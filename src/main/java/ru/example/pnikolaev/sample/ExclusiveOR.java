package ru.example.pnikolaev.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.example.pnikolaev.network.NeuralNetwork;
import ru.example.pnikolaev.network.NeuralNetworkImpl;
import ru.example.pnikolaev.network.NeuralNetworkBuilder;

import java.io.IOException;
import java.util.Arrays;

public class ExclusiveOR {
    static String fileName = "serfiles/ExclusiveOR.ser";
    static final Logger LOGGER = LoggerFactory.getLogger(ExclusiveOR.class);

    static class Training {
        public static void main(String[] args) throws IOException {
            NeuralNetwork nn = new NeuralNetworkBuilder.Builder()
                    .setCountInput(2)
                    .setCountOutput(1)
                    .setCountHidden(2)
                    .setCountLayer(3)
                    .setCoefficientTraining(.8)
                    .setBias(true).toBuild();
            nn.addTrain(new double[]{0, 0}, new double[]{0});
            nn.addTrain(new double[]{0, 1}, new double[]{1});
            nn.addTrain(new double[]{1, 0}, new double[]{1});
            nn.addTrain(new double[]{1, 1}, new double[]{0});
            nn.training(10000, .002);
            nn.save(fileName);
            getAnswer(nn);
        }
    }

    static class UseSerializable {
        public static void main(String[] args) throws IOException, ClassNotFoundException {
            NeuralNetworkImpl nn = NeuralNetworkImpl.init(fileName);
            getAnswer(nn);
        }
    }
    private static void getAnswer(NeuralNetwork nn) {
        LOGGER.info(Arrays.toString(nn.getAnswer(new double[]{1, 0})));
        LOGGER.info(Arrays.toString(nn.getAnswer(new double[]{1, 1})));
    }
}
