package BNN.network;

public class LearningPattern {
    private int[] array;
    private int expectedClass;
    private String fileName;

    public LearningPattern(int[] array, int expectedClass, String name) {
        this.array = array;
        this.expectedClass = expectedClass;
        this.fileName = name;
    }

    public int[] getArray() {
        return array;
    }

    public void setArray(int[] array) {
        this.array = array;
    }

    public int getExpectedClass() {
        return expectedClass;
    }

    public void setExpectedClass(int expectedClass) {
        this.expectedClass = expectedClass;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
