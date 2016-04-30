package wang.loveweibo.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class ImageUtils {
	public static final int GET_IMAGE_BY_CAMERA = 5001;
	public static final int GET_IMAGE_FROM_PHONE = 5002;
	public static Uri imageUriFromCamera;
	public static void showImagePickDialog(final Activity activity) {
		String title = "选择获取图片方式";
		String[] item = {"拍照", "相册"};
		new AlertDialog.Builder(activity).setTitle(title)
		.setItems(item, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					pickImageFromCamere(activity);
					break;
				case 1:
					pickImageFromAlbum(activity);
					break;
				default:
					break;
				}
			}
		}).show();
		
	}

	protected static void pickImageFromAlbum(Activity activity) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		activity.startActivityForResult(intent, GET_IMAGE_FROM_PHONE);
	}
	protected static void pickImageFromAlbum_1(Activity activity) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		activity.startActivityForResult(intent, GET_IMAGE_FROM_PHONE);
	}

	protected static void pickImageFromCamere(Activity activity) {
		imageUriFromCamera = createImageUri(activity);
		Intent intent = new Intent();
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriFromCamera);
		activity.startActivityForResult(intent, GET_IMAGE_BY_CAMERA);
	}

	private static Uri createImageUri(Context context) {
		String name = "LoveWbImg" + System.currentTimeMillis();
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, name);
		values.put(MediaStore.Images.Media.DISPLAY_NAME, name + ".jpeg");
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		return uri;
	}

	public static void delateImageUri(Context context,Uri uri) {
		context.getContentResolver().delete(uri, null, null);
	}
	public static String getImageAbsolutePath(Context context, Uri uri) {
		Cursor cursor = MediaStore.Images.Media.query(context.getContentResolver(), uri, new String[]{MediaStore.Images.Media.DATA});
		if(cursor.moveToFirst()) {
			return cursor.getString(0);
		}
		return null;
	}
}
