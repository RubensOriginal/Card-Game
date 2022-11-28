package poo;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageFactory {
	private static ImageFactory imgf = new ImageFactory();
	private Map<String, Image> images;

	private final int CARDHEIGHT = 307;
	private final int CARDWIDTH = 210;

	public static ImageFactory getInstance() {
		return imgf;
	}

	private ImageFactory() {
		images = new HashMap<>();
	}

	public ImageView createImage(String imgId, boolean realSize) {
		Image img = images.get(imgId);
		if (img == null) {
			// img = new Image("/cards/4031928.jpg");
			img = new Image(getClass().getResourceAsStream(imgId));
			images.put(imgId, img);
		}

		ImageView imgv = new ImageView(img);
		// imgv.setFitHeight(307);
		// imgv.setFitWidth(210);
		if (realSize) {
			imgv.setFitHeight(2 * CARDHEIGHT);
			imgv.setFitWidth(2 * CARDWIDTH);
		} else {
			imgv.setFitHeight(CARDHEIGHT / 4);
			imgv.setFitWidth(CARDWIDTH / 4);
		}
		return imgv;
	}
}
