package ru.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.network.Link;
import ru.network.NeuralNetwork;
import ru.network.NeuralNetworkBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class XYClassification {
    static String fileName = "serfiles/XYClassification.ser";
    static final Logger LOGGER = LoggerFactory.getLogger(XYClassification.class);

    static class Training {
        public static void main(String[] args) throws IOException {
            NeuralNetwork netWork = new NeuralNetworkBuilder.Builder()
                    .setUseLineChart(true)
                    .setCountInput(2)
                    .setCountOutput(1)
                    .setCountHidden(0)
                    .setCountLayer(2)
                    .setCoefficientTraining(0.03)
                    .setBias(true).toBuild();

            netWork.addTrain(new double[]{Math.random() * 10, 11}, new double[]{0});
            netWork.addTrain(new double[]{Math.random() * 10, 11}, new double[]{0});
            netWork.addTrain(new double[]{Math.random() * 10, 12}, new double[]{0});
            netWork.addTrain(new double[]{Math.random() * 10, 12}, new double[]{0});
            netWork.addTrain(new double[]{Math.random() * 10, 13}, new double[]{0});

            netWork.addTrain(new double[]{Math.random() * 10, 22}, new double[]{1});
            netWork.addTrain(new double[]{Math.random() * 10, 23}, new double[]{1});
            netWork.addTrain(new double[]{Math.random() * 10, 24}, new double[]{1});
            netWork.addTrain(new double[]{Math.random() * 10, 25}, new double[]{1});
            netWork.addTrain(new double[]{Math.random() * 10, 25}, new double[]{1});

            netWork.training(5000, 0.02);
            netWork.save(fileName);

            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{Math.random() * 10, 28})));
            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{Math.random() * 10, 24})));

            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{Math.random() * 10, 11})));
            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{Math.random() * 10, 13})));
        }
    }

    static class UseSerializable {
        public static void main(String[] args) throws IOException, ClassNotFoundException {
            NeuralNetwork netWork = NeuralNetwork.init(fileName);
            //Red
            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{4, 3})));
            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{5, 1})));
            //Blue
            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{1, 2})));
            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{2, 5})));
        }
    }

    static class Training1 {
        public static void main(String[] args) throws IOException {
            NeuralNetwork netWork = new NeuralNetworkBuilder.Builder()
                    .setUseLineChart(true)
                    .setCountInput(2)
                    .setCountOutput(1)
                    .setCountHidden(6)
                    .setCountLayer(4)
                    .setCoefficientTraining(.01)
                    .setBias(true).toBuild();
            //Red (x,y)
            for (int i = 0; i < 40; i++) {
                netWork.addTrain(new double[]{25 + Math.random() * (5 * (new Random().nextBoolean() ? 1 : -1))
                        , 25 + Math.random() * (5 * (new Random().nextBoolean() ? 1 : -1))}, new double[]{0});
            }

            for (int i = 0; i < 20; i++) {
                netWork.addTrain(new double[]{35 + Math.random() * 10
                        , 25 + Math.random() * (10 * (new Random().nextBoolean() ? 1 : -1))}, new double[]{1});
            }
            for (int i = 0; i < 20; i++) {
                netWork.addTrain(new double[]{15 - Math.random() * 10
                        , 25 + Math.random() * (10 * (new Random().nextBoolean() ? 1 : -1))}, new double[]{1});
            }

            for (int i = 0; i < 40; i++) {
                netWork.addTrain(new double[]{25 + Math.random() * (10 * (new Random().nextBoolean() ? 1 : -1))
                        , 35 + Math.random() * 10}, new double[]{1});
            }

            for (int i = 0; i < 40; i++) {
                netWork.addTrain(new double[]{25 + Math.random() * (10 * (new Random().nextBoolean() ? 1 : -1))
                        , 15 - Math.random() * 10}, new double[]{1});
            }
            //Blue (x,y)
//            netWork.addTrain(new double[]{20 + Math.random() * (10 * (5 + (new Random().nextBoolean() ? 1 : -1)))
//                    , 20 + Math.random() * (5 * (5 + (new Random().nextBoolean() ? 1 : -1)))}, new double[]{1});

            netWork.training(0, 0.01);
            netWork.save(fileName);
        }
    }
}
