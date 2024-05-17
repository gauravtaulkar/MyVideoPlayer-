package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    MediaPlayer player;

    @FXML
    private MediaView mediaView;

    @FXML
    private Button playBtn;

    @FXML
    private Button preBtn;

    @FXML
    private Button nextBtn;

    @FXML
    private Slider timeSlider;

    @FXML
    void openSongMenu(ActionEvent event) {
        try {
            System.out.println("open song clicked");
            FileChooser chooser = new FileChooser();
            File file = chooser.showOpenDialog(null);

            if (file != null) { // Check if a file is selected
                Media m = new Media(file.toURI().toString());

                if (player != null) {
                    player.dispose();
                }

                player = new MediaPlayer(m);
                mediaView.setMediaPlayer(player);

                // Rest of your code...
                player.setOnReady(() -> {
                    // When player gets ready...
                    timeSlider.setMin(0);
                    timeSlider.setMax(player.getMedia().getDuration().toMinutes());

                    timeSlider.setValue(0);
                    try {
                        playBtn.setGraphic(new ImageView(new Image(new FileInputStream("MyVideoPlayer-master\\src\\icons\\play.png"))));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                });

                // Listener on player...
                player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                    @Override
                    public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                        // Coding...
                        Duration d = player.getCurrentTime();
                        timeSlider.setValue(d.toMinutes());
                    }
                });

                // Time slider
                timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        if (timeSlider.isPressed()) {
                            double val = timeSlider.getValue();
                            player.seek(new Duration(val * 60 * 1000));
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void play(ActionEvent event) {
        try {
            MediaPlayer.Status status = player.getStatus();

            if (status == MediaPlayer.Status.PLAYING) {
                player.pause();
                playBtn.setGraphic(new ImageView(new Image(new FileInputStream("MyVideoPlayer-master\\src\\icons\\play.png"))));
            } else {
                player.play();
                playBtn.setGraphic(new ImageView(new Image(new FileInputStream("MyVideoPlayer-master\\src\\icons\\pause.png"))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            playBtn.setGraphic(new ImageView(new Image(new FileInputStream("MyVideoPlayer-master\\src\\icons\\play.png"))));
            preBtn.setGraphic(new ImageView(new Image(new FileInputStream("MyVideoPlayer-master\\src\\icons\\previous.png"))));
            nextBtn.setGraphic(new ImageView(new Image(new FileInputStream("MyVideoPlayer-master\\src\\icons\\next.png"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void preBtnClick(ActionEvent event) {
        double d = player.getCurrentTime().toSeconds();
        d = d - 10;
        player.seek(new Duration(d * 1000));
    }

    @FXML
    void nextBtnClick(ActionEvent event) {
        double d = player.getCurrentTime().toSeconds();
        d = d + 10;
        player.seek(new Duration(d * 1000));
    }
}
