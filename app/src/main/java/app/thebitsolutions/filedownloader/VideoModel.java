package app.thebitsolutions.filedownloader;

public class VideoModel {
    private String Title,Name;

    public VideoModel(String title, String name) {
        Title = title;
        Name = name;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
