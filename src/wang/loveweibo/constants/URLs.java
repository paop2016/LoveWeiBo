package wang.loveweibo.constants;

public interface URLs {
	String BASE_URL = "https://api.weibo.com/2/";
	String statusesHome_timeline = BASE_URL+"statuses/home_timeline.json";
	String commentsShow = BASE_URL + "comments/show.json";
	String commentsCreate = BASE_URL + "comments/create.json";
	// 转发一条微博
	String statusesRepost = BASE_URL + "statuses/repost.json";
	// 发布一条微博(带图片)
	String statusesUpload = BASE_URL + "statuses/upload.json";
	// 发布一条微博(不带图片)
	String statusesUpdate = BASE_URL + "statuses/update.json";
}
