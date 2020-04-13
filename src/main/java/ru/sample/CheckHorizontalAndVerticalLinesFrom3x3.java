package ru.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.network.NeuralNetwork;
import ru.network.NeuralNetworkBuilder;

import java.io.IOException;
import java.util.Arrays;

public class CheckHorizontalAndVerticalLinesFrom3x3 {
    static String fileName = "serfiles/CheckHorizontalAndVerticalLinesFrom3x3.ser";
    static final Logger LOGGER = LoggerFactory.getLogger(CheckHorizontalAndVerticalLinesFrom3x3.class);

    static class Training {
        public static void main(String[] args) throws IOException {
            NeuralNetwork netWork = new NeuralNetworkBuilder.Builder()
                    .setUseLineChart(true)
                    .setCountInput(9)
                    .setCountOutput(2)
                    .setCountHidden(6)
                    .setCountLayer(4)
                    .setBias(true)
                    .toBuild();
            // Horizontal lines
            netWork.addTrain(new double[]{1, 1, 1, 0, 0, 0, 0, 0, 0}, new double[]{1, 0});
            netWork.addTrain(new double[]{0, 0, 0, 1, 1, 1, 0, 0, 0}, new double[]{1, 0});
            netWork.addTrain(new double[]{0, 0, 0, 0, 0, 0, 1, 1, 1}, new double[]{1, 0});
            // Vertical lines
            netWork.addTrain(new double[]{1, 0, 0, 1, 0, 0, 1, 0, 0}, new double[]{0, 1});
            netWork.addTrain(new double[]{0, 1, 0, 0, 1, 0, 0, 1, 0}, new double[]{0, 1});
            netWork.addTrain(new double[]{0, 0, 1, 0, 0, 1, 0, 0, 1}, new double[]{0, 1});

            // Horizontal & Vertical lines
            netWork.addTrain(new double[]{1, 1, 1, 1, 0, 0, 1, 0, 0}, new double[]{1, 1});
            netWork.addTrain(new double[]{1, 1, 1, 0, 1, 0, 0, 1, 0}, new double[]{1, 1});
            netWork.addTrain(new double[]{1, 1, 1, 0, 0, 1, 0, 0, 1}, new double[]{1, 1});

            netWork.addTrain(new double[]{1, 0, 0, 1, 1, 1, 1, 0, 0}, new double[]{1, 1});
            netWork.addTrain(new double[]{0, 1, 0, 1, 1, 1, 0, 1, 0}, new double[]{1, 1});
            netWork.addTrain(new double[]{0, 0, 1, 1, 1, 1, 0, 0, 1}, new double[]{1, 1});

            netWork.addTrain(new double[]{1, 0, 0, 1, 0, 0, 1, 1, 1}, new double[]{1, 1});
            netWork.addTrain(new double[]{0, 1, 0, 0, 1, 0, 1, 1, 1}, new double[]{1, 1});
            netWork.addTrain(new double[]{0, 0, 1, 0, 0, 1, 1, 1, 1}, new double[]{1, 1});

            // No lines
            netWork.addTrain(new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0}, new double[]{0, 0});

            netWork.training(100000, 0.005);
            netWork.save(fileName);
            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{1, 0, 0, 0, 1, 0, 1, 1, 1})));
            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0})));
        }
    }

    static class UseSerializable {
        public static void main(String[] args) throws IOException, ClassNotFoundException {
            NeuralNetwork netWork = NeuralNetwork.init(fileName);
            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{1, 0, 0, 0, 1, 0, 1, 1, 1})));

            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0})));

            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{0, 0, 1, 0, 0, 1, 0, 0, 1})));

            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{0, 0, 0, 1, 1, 1, 0, 0, 0})));
        }
    }
}
