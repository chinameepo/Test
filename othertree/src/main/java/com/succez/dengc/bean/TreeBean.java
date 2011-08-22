package com.succez.dengc.bean; 

	/**
	 * All right resrvered esensoft(2011)
	 * 
	 * @author 邓超 deng.369@gmail.com
	 * @version 1.0,创建时间：2011-8-18 上午08:49:56
	 * @since jdk1.6 一个用来在tTree中表示一个节点的bean。
	 */
	public class TreeBean {
		private int id;
		private int prentid;
		private String name;
		private String url;
		private String title;
		private String target;
		private String image;

		public TreeBean(int id, int prentid, String name, String url,
				String targert, String image) {
			this.id = id;
			this.prentid = prentid;
			this.name = name;
			this.url = url;
			this.target = targert;
			this.image = image;
		}

		public TreeBean(int id, int prentid, String name, String url) {
			this(id, prentid, name, url, null, null);
		}

		public TreeBean(int id, int prentid, String name) {
			this(id, prentid, name, null);
		}
		public TreeBean(){
			
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getPrentid() {
			return prentid;
		}

		public void setPrentid(int prentid) {
			this.prentid = prentid;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getTarget() {
			return target;
		}

		public void setTarget(String target) {
			this.target = target;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}
	}

