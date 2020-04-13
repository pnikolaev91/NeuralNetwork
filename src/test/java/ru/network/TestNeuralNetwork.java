package ru.network;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TestNeuralNetwork {
    @Test
    public void testCreateDefaultNetwork() {
        NeuralNetwork netWork = new NeuralNetworkBuilder.Builder().setBias(false).toBuild();
        Assert.assertEquals("Invalid amount by layers", netWork.neurons.size(), 2);
        Assert.assertEquals("Invalid amount by neurons", netWork.neurons.stream().mapToInt(List::size).sum(), 2);
        Assert.assertEquals("Invalid amount by links", netWork.neurons.stream().mapToInt(l -> l.stream().mapToInt(n -> n.getOutComingLinks().size()).sum()).sum(), 1);
        Assert.assertTrue(checkLinkNeurons(netWork));
        Assert.assertTrue(checkInitWeight(netWork));
    }

    @Test
    public void testCreateHiddenLayer() {
        NeuralNetwork netWork = new NeuralNetworkBuilder.Builder().setBias(false).setCountHidden(1).setCountLayer(3).toBuild();
        Assert.assertEquals("Invalid amount by layers", netWork.neurons.size(), 3);
        Assert.assertEquals("Invalid amount by neurons", netWork.neurons.stream().mapToInt(List::size).sum(), 3);
        Assert.assertEquals("Invalid amount by links", netWork.neurons.stream().mapToInt(l -> l.stream().mapToInt(n -> n.getOutComingLinks().size()).sum()).sum(), 2);
        Assert.assertTrue(checkLinkNeurons(netWork));
        Assert.assertTrue(checkInitWeight(netWork));
    }

    @Test
    public void testCreateHiddenLayerAndBias() {
        NeuralNetwork netWork = new NeuralNetworkBuilder.Builder().setCountHidden(1).setCountLayer(3).toBuild();
        Assert.assertEquals("Invalid amount by layers", netWork.neurons.size(), 3);
        Assert.assertEquals("Invalid amount by neurons", netWork.neurons.stream().mapToInt(List::size).sum(), 5);
        Assert.assertEquals("Invalid amount by links", netWork.neurons.stream().mapToInt(l -> l.stream().mapToInt(n -> n.getOutComingLinks().size()).sum()).sum(), 4);
        Assert.assertTrue(checkLinkNeurons(netWork));
        Assert.assertTrue(checkInitWeight(netWork));
    }

    private boolean checkInitWeight(NeuralNetwork nw) {
        return nw.neurons.stream()
                .anyMatch(l -> l.stream().anyMatch(n -> n.getOutComingLinks().stream()
                        .anyMatch(link -> link.getWeight() != 0 && link.getWeight() < 1)));
    }

    private boolean checkLinkNeurons(NeuralNetwork nw) {
        return nw.neurons.stream().anyMatch(l -> l.stream().anyMatch(this::check));
    }

    private boolean check(Neuron n) {
        if (n.getOutComingLinks().size() == 0 && n.getNeuronType().equals(NeuronType.OUTPUT)) {
            return true;
        } else {
            return n.getOutComingLinks().stream().anyMatch(l -> check(l.getOutNeuron()));
        }
    }
}
