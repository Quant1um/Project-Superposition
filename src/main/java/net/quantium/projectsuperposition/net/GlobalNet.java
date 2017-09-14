package net.quantium.projectsuperposition.net;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.quantium.projectsuperposition.utilities.vector.IntegerVector3;
import net.quantium.projectsuperposition.utilities.vector.VectorUtils;

public class GlobalNet<T extends INetNode> {
	private WeakReference<World> world;
	
	private Map<IntegerVector3, WeakReference<Net<T>>> netPool = new HashMap<IntegerVector3, WeakReference<Net<T>>>();
	public List<Net<T>> nets = new ArrayList<Net<T>>();
	
	public GlobalNet(World w){
		this.world = new WeakReference(w);
	}
	
	
	public Net<T> getNet(IntegerVector3 v){
		WeakReference<Net<T>> w = netPool.get(v);
		if(w != null) return w.get();
		return null;
	}
	
	public T get(IntegerVector3 v){
		Net<T> net = getNet(v);
		if(net != null) return net.get(v);
		return null;
	}
	
	private void add(IntegerVector3 v, T t){
		Net<T> net = getNet(v);
		if(net != null) return;
		net = new Net<T>(this);
		nets.add(net);
		netPool.put(v, new WeakReference(net));
		net.add(v, t);
	}
	
	public void addNode(IntegerVector3 v, T t){
		add(v, t);
		merge(merge(v), getNet(v));
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
		System.out.println(t + " added to " + v + ". Nets: " + nets.size());
	}
	
	public void removeNode(IntegerVector3 v){
		split(v);
		remove(v);
		System.out.println("Node removed from " + v + ". Nets: " + nets.size());
	}
	
	private void merge(Net<T> net, Net<T> net1){
		if(net != null && net1 != null){
			if(net1 != net) {
				for (Entry<IntegerVector3, T> entry : net1.get().entrySet()){
					net.add(entry.getKey(), entry.getValue());
					netPool.put(entry.getKey(), new WeakReference(net)); //can be memory leak
				}
				nets.remove(net1);
			}
		}
	}
	
	public Net<T> merge(IntegerVector3 point){
		List<Net<T>> list = getAdjacentNets(point);
		if(list.size() <= 0) return null;
		Net<T> main = list.get(0);
		for(int i = 1; i < list.size(); i++) merge(main, list.get(i));
		return main;
	}
	
	public List<Net<T>> getAdjacentNets(IntegerVector3 point){
		List<Net<T>> list = new ArrayList<Net<T>>();
		List<IntegerVector3> list2 = getAdjacentPoints(point);
		for(int i = 0; i < list2.size(); i++) list.add(getNet(list2.get(i)));
		return list;
	}

	public List<IntegerVector3> getAdjacentPoints(IntegerVector3 point){
		List<IntegerVector3> list = new ArrayList<IntegerVector3>();
		for(int i = 0; i < 6; i++){
			ForgeDirection d = ForgeDirection.getOrientation(i);
			IntegerVector3 v = new IntegerVector3(VectorUtils.add(point, new IntegerVector3(d.offsetX, d.offsetY, d.offsetZ)));
			Net<T> net = getNet(v);
			if(net != null && get(v) != null)
				if(get(v).connection(world.get(), v, d.getOpposite())) list.add(v);
		}
		return list;
	}
	
	private void remove(IntegerVector3 v){
		Net<T> net = getNet(v);
		if(net == null) return;
		net.remove(v);
		if(net.get().size() <= 0) nets.remove(net);
	}
	
	public World getWorld(){
		return world.get();
	}
	
	//many parts of there code are taken from GalacticraftCore, thanks, micdoodle8!)
	public void split(IntegerVector3 point){
		Net<T> net = getNet(point);
		if(net == null) return;
	    if(net.get().size() > 1){
	    	IntegerVector3[] nextToSplit = new IntegerVector3[6];
	        for(int i = 0; i < 6; i++){
				ForgeDirection d = ForgeDirection.getOrientation(i);
				IntegerVector3 v = new IntegerVector3(VectorUtils.add(point, new IntegerVector3(d.offsetX, d.offsetY, d.offsetZ)));
				Net<T> net2 = getNet(v);
				if(net2 != null && get(v) != null)
					if(get(v).connection(world.get(), v, d.getOpposite()))
						nextToSplit[i] = v;
	        }
	        for(int i1 = 0; i1 < 6; i1++){
	        	if (nextToSplit[i1] != null){
	        		List<NodePositionTuple> partNetwork = new NetworkFinder(world.get(), nextToSplit[i1], point).exploreNetwork();
	        		for(int i2 = i1 + 1; i2 < 6; i2++)
	                	if (nextToSplit[i2] != null) 
	                		for(int i3 = 0; i3 < partNetwork.size(); i3++)
	                			if(partNetwork.get(i3).getPosition().equals(nextToSplit[i2])) nextToSplit[i2] = null;
	              	Net<T> net1 = new Net<T>(this);
	              	for(int i3 = 0; i3 < partNetwork.size(); i3++){
	              		net1.get().put(partNetwork.get(i3).getPosition(), partNetwork.get(i3).node);
	              		netPool.put(partNetwork.get(i3).getPosition(), new WeakReference(net1));
	              	}
	              	nets.add(net1);
	        	}
	        }
	    }
	}
	
	private class NodePositionTuple{
		private final T node;
		private final IntegerVector3 position;
		
		public NodePositionTuple(T node, IntegerVector3 position){
			this.node = node;
			this.position = position;
		}
		
		public T getNode(){
			return node;
		}
		
		public IntegerVector3 getPosition(){
			return position;
		}
	}
	

	private class NetworkFinder{
		private World world;
		private IntegerVector3 start;
		private IntegerVector3 ignore;
		private List<IntegerVector3> iterated = new ArrayList<IntegerVector3>();
		private List<NodePositionTuple> found = new ArrayList<NodePositionTuple>();
  
		public NetworkFinder(World world, IntegerVector3 start, IntegerVector3 ignore){
			this.start = start;
			this.ignore = ignore;
			this.world = world;
		}
  
		private void loopAll(int x, int y, int z, int dirIn){
			for (int i = 0; i < 6; i++) {
				if(i == dirIn) continue;
				ForgeDirection d = ForgeDirection.getOrientation(i);
				IntegerVector3 v = new IntegerVector3(VectorUtils.add(new IntegerVector3(x, y, z), new IntegerVector3(d.offsetX, d.offsetY, d.offsetZ)));
				if (!this.iterated.contains(v)){
					this.iterated.add(v);
					Net<T> net = getNet(v);
					if(net != null && get(v) != null)
						if(get(v).connection(world, v, d.getOpposite())){
							this.found.add(new NodePositionTuple(get(v), v));
	            			loopAll(v.x(), v.y(), v.z(), d.getOpposite().ordinal());
						}
				}
			}
		}
  
		public List<NodePositionTuple> exploreNetwork(){
	  		if (get(this.start) != null){
    			this.iterated.add(this.start);
      			this.iterated.add(this.ignore);
      			this.found.add(new NodePositionTuple(get(this.start), this.start));
      			loopAll(this.start.x(), this.start.y(), this.start.z(), 6);
    		}
    		return this.found;
  		}
	}
}
