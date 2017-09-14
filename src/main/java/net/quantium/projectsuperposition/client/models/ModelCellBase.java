// Date: 05.04.2016 19:22:27
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX






package net.quantium.projectsuperposition.client.models;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCellBase extends ModelBase
{
    ModelRenderer ShapeA;
    ModelRenderer ShapeB;
    ModelRenderer ShapeC;
    ModelRenderer ShapeD;
    ModelRenderer ShapeE;
    ModelRenderer ShapeF;
  
  public ModelCellBase()
  {
    textureWidth = 64;
    textureHeight = 32;
    
      ShapeA = new ModelRenderer(this, 0, 0);
      ShapeA.addBox(0F, 0F, 0F, 3, 1, 3);
      ShapeA.setRotationPoint(-1F, 0F, -1F);
      ShapeA.setTextureSize(64, 32);
      ShapeA.mirror = true;
      setRotation(ShapeA, 0F, 0F, 0F);
      ShapeB = new ModelRenderer(this, 0, 0);
      ShapeB.addBox(0F, 0F, 0F, 3, 1, 3);
      ShapeB.setRotationPoint(-1F, -5F, -1F);
      ShapeB.setTextureSize(64, 32);
      ShapeB.mirror = true;
      setRotation(ShapeB, 0F, 0F, 0F);
      ShapeC = new ModelRenderer(this, 0, 0);
      ShapeC.addBox(0F, 0F, 0F, 1, 4, 1);
      ShapeC.setRotationPoint(-1F, -4F, 1F);
      ShapeC.setTextureSize(64, 32);
      ShapeC.mirror = true;
      setRotation(ShapeC, 0F, 0F, 0F);
      ShapeD = new ModelRenderer(this, 0, 0);
      ShapeD.addBox(0F, 0F, 0F, 1, 4, 1);
      ShapeD.setRotationPoint(1F, -4F, 1F);
      ShapeD.setTextureSize(64, 32);
      ShapeD.mirror = true;
      setRotation(ShapeD, 0F, 0F, 0F);
      ShapeE = new ModelRenderer(this, 0, 0);
      ShapeE.addBox(0F, 0F, 0F, 1, 4, 1);
      ShapeE.setRotationPoint(1F, -4F, -1F);
      ShapeE.setTextureSize(64, 32);
      ShapeE.mirror = true;
      setRotation(ShapeE, 0F, 0F, 0F);
      ShapeF = new ModelRenderer(this, 0, 0);
      ShapeF.addBox(0F, 0F, 0F, 1, 4, 1);
      ShapeF.setRotationPoint(-1F, -4F, -1F);
      ShapeF.setTextureSize(64, 32);
      ShapeF.mirror = true;
      setRotation(ShapeF, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5,entity);
    ShapeA.render(f5);
    ShapeB.render(f5);
    ShapeC.render(f5);
    ShapeD.render(f5);
    ShapeE.render(f5);
    ShapeF.render(f5);
    
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5,Entity e)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5,e);
  }

}
