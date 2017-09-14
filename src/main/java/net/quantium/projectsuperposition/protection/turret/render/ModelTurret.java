package net.quantium.projectsuperposition.protection.turret.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTurret extends ModelBase
{
  //fields
    ModelRenderer Base;
    ModelRenderer Rotator;
    ModelRenderer Handle0;
    ModelRenderer Handle1;
    ModelRenderer Stator0;
    ModelRenderer Stator1;
    ModelRenderer Ejector;
  
  public ModelTurret()
  {
	  textureWidth = 64;
	  textureHeight = 32;
    
      Base = new ModelRenderer(this, 0, 0);
      Base.addBox(0F, 0F, 0F, 10, 2, 10);
      Base.setRotationPoint(-5F, 6F, -5F);
      Base.setTextureSize(64, 32);
      Base.mirror = true;
      setRotation(Base, 0F, 0F, 0F);
      Rotator = new ModelRenderer(this, 0, 0);
      Rotator.addBox(-5F, 0F, -5F, 10, 1, 10);
      Rotator.setRotationPoint(0F, 5F, 0F);
      Rotator.setTextureSize(64, 32);
      Rotator.mirror = true;
      setRotation(Rotator, 0F, 45F, 0F);
      Handle0 = new ModelRenderer(this, 0, 0);
      Handle0.addBox(2F, -9F, -1F, 1, 8, 2);
      Handle0.setRotationPoint(0F, 5F, 0F);
      Handle0.setTextureSize(64, 32);
      Handle0.mirror = true;
      setRotation(Handle0, 0F, 0F, 0F);
      Handle1 = new ModelRenderer(this, 0, 0);
      Handle1.addBox(-3F, -9F, -1F, 1, 8, 2);
      Handle1.setRotationPoint(0F, 5F, 0F);
      Handle1.setTextureSize(64, 32);
      Handle1.mirror = true;
      setRotation(Handle1, 0F, 0F, 0F);
      Stator0 = new ModelRenderer(this, 0, 0);
      Stator0.addBox(-4F, -1F, -4F, 8, 1, 8);
      Stator0.setRotationPoint(0F, 5F, 0F);
      Stator0.setTextureSize(64, 32);
      Stator0.mirror = true;
      setRotation(Stator0, 0F, 0F, 0F);
      Ejector = new ModelRenderer(this, 16, 0).setTextureSize(32, 32);
      Ejector.addBox(-2F, 0F, -4F, 4, 4, 8);
      Ejector.setRotationPoint(0F, -4F, 0F);
      Ejector.mirror = true;
      setRotation(Ejector, 0F, 0F, 0F);

  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Base.render(f5);
    Rotator.render(f5);
    Handle0.render(f5);
    Handle1.render(f5);
    Stator0.render(f5);
    Ejector.render(f5);
  }
 
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
    if(f2 < 0.5){
    	Rotator.rotateAngleY = Handle0.rotateAngleY = Handle1.rotateAngleY = Stator0.rotateAngleY = Ejector.rotateAngleY = f;//= Minecraft.getSystemTime() / 1000f;
    	Ejector.rotateAngleX = f1;
    }else{
    	Rotator.rotateAngleY = Handle0.rotateAngleY = Handle1.rotateAngleY = Stator0.rotateAngleY = Ejector.rotateAngleY = (float) (-f + Math.PI);//= Minecraft.getSystemTime() / 1000f;
    	Ejector.rotateAngleX = -f1;
    }
  }

}
