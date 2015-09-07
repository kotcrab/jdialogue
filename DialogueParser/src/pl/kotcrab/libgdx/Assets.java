package pl.kotcrab.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Assets {
	public static ShaderProgram fontDistanceFieldShader;
	public static BitmapFont consolasFont;

	public static void load () {
		fontDistanceFieldShader = new ShaderProgram(Gdx.files.internal("assets/font/font.vert"), Gdx.files.internal("assets/font/font.frag"));
		if (!fontDistanceFieldShader.isCompiled()) {
			Gdx.app.error("fontShader", "compilation failed:\n" + fontDistanceFieldShader.getLog());
		}

		Texture fontTexture = new Texture(Gdx.files.internal("assets/font/consolas.png"), true);
		fontTexture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);

		consolasFont = new BitmapFont(Gdx.files.internal("assets/font/consolas.fnt"), new TextureRegion(fontTexture), false);

		Gdx.app.log("Assets", "Finished loading assets");
	}

	public static void dispose () {
		consolasFont.dispose();
		fontDistanceFieldShader.dispose();
		Gdx.app.log("Assets", "Finished disposing assets");
	}
}
