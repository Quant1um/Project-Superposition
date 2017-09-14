package net.quantium.projectsuperposition.protection.field.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelField extends ModelBase
{
  //fields
    ModelRenderer Base;
    ModelRenderer Rotator;
    ModelRenderer Handle;
    ModelRenderer[] Coil;
    
  public ModelField()
  {
    textureWidth = 64;
    textureHeight = 32;
    
      Base = new ModelRenderer(this, 32, 0).setTextureSize(512, 64);
      Base.addBox(0F, 0F, 0F, 10, 2, 10);
      Base.setRotationPoint(-5F, 6F, -5F);
      //Base.mirror = true;
      setRotation(Base, 0F, 0F, 0F);
      Rotator = new ModelRenderer(this, 0, 0);
      Rotator.addBox(-4F, 0F, -4F, 8, 1, 8);
      Rotator.setRotationPoint(0F, 5F, 0F);
      Rotator.setTextureSize(128, 32);
      Rotator.mirror = true;
      setRotation(Rotator, 0F, 0F, 0F);
      Handle = new ModelRenderer(this, 0, 0);
      Handle.addBox(1F, -4.5F, 1F, 2, 12, 2);
      Handle.setRotationPoint(0F, 0f, 0F);
      Handle.setTextureSize(128, 32);
      Handle.mirror = true;
      setRotation(Handle, 0F, 0F, 0F);
	  Coil = new ModelRenderer[7];
      for(int i = 0; i < 7; i++){
    	  Coil[i] = new ModelRenderer(this, 0, 0);
    	  Coil[i].setTextureOffset(32, 0);
    	  Coil[i].addBox(0F, -5F, 0F, 4, 1, 4);
    	  Coil[i].setRotationPoint(0F, 0F + i * 1.25f, 0F);
    	  Coil[i].setTextureSize(256, 64);
    	  Coil[i].mirror = true;
      }
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Base.render(f5);
    Rotator.render(f5);
    Handle.render(f5);
    for(int i = 0; i < Coil.length; i++)
    	Coil[i].render(f5);
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
  }
}
