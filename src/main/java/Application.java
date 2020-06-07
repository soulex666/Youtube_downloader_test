import com.github.kiulian.downloader.OnYoutubeDownloadListener;
import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.YoutubeException;
import com.github.kiulian.downloader.model.VideoDetails;
import com.github.kiulian.downloader.model.YoutubeVideo;
import com.github.kiulian.downloader.model.formats.AudioFormat;
import com.github.kiulian.downloader.model.formats.AudioVideoFormat;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Future;

public class Application {

       public static void main(String[] args) throws IOException, YoutubeException, InterruptedException {



        FileReader readFromFile = new FileReader("youtubeLinks.txt");
        Scanner reader = new Scanner(readFromFile);
        String tempLine = (reader.nextLine().split(".+v=")[1]).split("&.+")[0];



        reader.close();
        System.out.println(tempLine);
        System.out.println("Test test");



        String videoID = tempLine;
        YoutubeDownloader downloader = new YoutubeDownloader();
        YoutubeVideo video = downloader.getVideo(videoID);
        VideoDetails details = video.details();
        System.out.println("Title: " + details.title());
        System.out.println("Description: " + details.description());
        List<AudioVideoFormat> videoWithAudioFormat = video.videoWithAudioFormats();
        videoWithAudioFormat.forEach(it -> {
            System.out.println("Video&Audio: " + it.videoQuality() + ": " + it.url());
        });
        List<AudioFormat> audioFormats = video.audioFormats();
        audioFormats.forEach(it -> {
            System.out.println("Audio: " + it.audioQuality() + ": " + it.url());
        });

        File outputDirVideoWithAudio = new File("video_audio");
        //videoWithAudioFormat.get(1) <- number of link list
        video.downloadAsync(videoWithAudioFormat.get(0), outputDirVideoWithAudio, new OnYoutubeDownloadListener() {
            @Override
            public void onDownloading(int progress) {
                System.out.printf("\b\b\b\b\b%d%%", progress);
            }

            @Override
            public void onFinished(File file) {
                System.out.printf("\nFinish video: %s", file);
                System.exit(0);  //exit from JVM progress

            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("Error: " + throwable.getMessage());
            }
        });

/*        File outputDirAudio = new File("audio");
        video.downloadAsync(audioFormats.get(0), outputDirAudio, new OnYoutubeDownloadListener() {
            @Override
            public void onDownloading(int progress) {
                System.out.printf("\b\b\b\b\b%d%%", progress);
            }

            @Override
            public void onFinished(File file) {
                System.out.printf("\nFinish audio: %s", file);
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("Error: " + throwable.getMessage());
            }
        });*/

        Thread.currentThread().join();

    }
}
