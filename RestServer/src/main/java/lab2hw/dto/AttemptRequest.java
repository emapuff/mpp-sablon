package lab2hw.dto;

public class AttemptRequest {
    private int idx1;
    private int idx2;

    public AttemptRequest() {}
    public AttemptRequest(int idx1, int idx2) {
        this.idx1 = idx1;
        this.idx2 = idx2;
    }

    public int getPositionOne() {
        return idx1;
    }

    public void setIdx1(int idx1) {
        this.idx1 = idx1;
    }

    public int getPositionTwo() {
        return idx2;
    }

    public void setIdx2(int idx2) {
        this.idx2 = idx2;
    }
}
