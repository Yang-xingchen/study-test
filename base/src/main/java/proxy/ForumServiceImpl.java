package proxy;

public class ForumServiceImpl implements ForumService {


	@Override
	public void removeTopic(int topicId) {
		System.out.println("removeTopic:" + topicId);
	}

	@Override
	public void removeForum(int forumId) {
		System.out.println("removeForum:" + forumId);
	}

	public String test() {
		System.out.println("this is ForumServiceImpl");
		return "this is ForumServiceImpl : " + this.toString();
	}

}
