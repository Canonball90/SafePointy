package thefellas.safepoint.utils.particle.neww;

import javax.vecmath.Tuple2f;
import javax.vecmath.Vector2f;
import net.minecraft.client.gui.ScaledResolution;
import thefellas.safepoint.modules.core.AC_ClickGui;
import thefellas.safepoint.utils.ColorUtil;
import thefellas.safepoint.utils.RenderUtil;

public final class ParticleSystem
{
    private final int PARTS = 200;
    private final Particle[] particles;
    private ScaledResolution scaledResolution;
    
    public ParticleSystem(final ScaledResolution scaledResolution) {
        this.particles = new Particle[200];
        this.scaledResolution = scaledResolution;
        for (int i = 0; i < 200; ++i) {
            this.particles[i] = new Particle(new Vector2f((float)(Math.random() * scaledResolution.getScaledWidth()), (float)(Math.random() * scaledResolution.getScaledHeight())));
        }
    }
    
    public void update() {
        for (int i = 0; i < 200; ++i) {
            final Particle particle = this.particles[i];
            if (this.scaledResolution != null) {
                final boolean isOffScreenX = particle.getPos().x > this.scaledResolution.getScaledWidth() || particle.getPos().x < 0.0f;
                final boolean isOffScreenY = particle.getPos().y > this.scaledResolution.getScaledHeight() || particle.getPos().y < 0.0f;
                if (isOffScreenX || isOffScreenY) {
                    particle.respawn(this.scaledResolution);
                }
            }
            particle.update();
        }
    }
    
    public void render(final int mouseX, final int mouseY) {
        if (!AC_ClickGui.getInstance().particles.getValue()) {
            return;
        }
        for (int i = 0; i < 200; ++i) {
            final Particle particle = this.particles[i];
            for (int j = 1; j < 200; ++j) {
                if (i != j) {
                    final Particle otherParticle = this.particles[j];
                    final Vector2f diffPos = new Vector2f(particle.getPos());
                    diffPos.sub((Tuple2f)otherParticle.getPos());
                    final float diff = diffPos.length();
                    final int distance = 200 / ((this.scaledResolution.getScaleFactor() <= 1) ? 3 : this.scaledResolution.getScaleFactor());
                    if (diff < distance) {
                        final int lineAlpha = (int)map(diff, distance, 0.0, 0.0, 127.0);
                        if (lineAlpha > 8) {
                            RenderUtil.drawLine(particle.getPos().x + particle.getSize() / 2.0f, particle.getPos().y + particle.getSize() / 2.0f, otherParticle.getPos().x + otherParticle.getSize() / 2.0f, otherParticle.getPos().y + otherParticle.getSize() / 2.0f, 1.0f, Particle.changeAlpha(ColorUtil.toRGBA(AC_ClickGui.getInstance().color.getColor().getRed(),AC_ClickGui.getInstance().color.getColor().getGreen(),AC_ClickGui.getInstance().color.getColor().getBlue()), lineAlpha));
                        }
                    }
                }
            }
            particle.render(mouseX, mouseY);
        }
    }
    
    public static double map(double value, final double a, final double b, final double c, final double d) {
        value = (value - a) / (b - a);
        return c + value * (d - c);
    }
    
    public ScaledResolution getScaledResolution() {
        return this.scaledResolution;
    }
    
    public void setScaledResolution(final ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
    }
}
