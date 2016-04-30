package wang.loveweibo.constants;

public interface URLs {
	String BASE_URL = "https://api.weibo.com/2/";
	String statusesHome_timeline = BASE_URL+"statuses/home_timeline.json";
	String commentsShow = BASE_URL + "comments/show.json";
	String commentsCreate = BASE_URL + "comments/create.json";
	// ת��һ��΢��
	String statusesRepost = BASE_URL + "statuses/repost.json";
	// ����һ��΢��(��ͼƬ)
	String statusesUpload = BASE_URL + "statuses/upload.json";
	// ����һ��΢��(����ͼƬ)
	String statusesUpdate = BASE_URL + "statuses/update.json";
}
