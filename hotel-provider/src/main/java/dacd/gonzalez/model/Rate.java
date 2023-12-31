package dacd.gonzalez.model;

public class Rate {
    private final String code;
    private final  String rateName;
    private final int rate;
    private final int tax;

    public Rate(String code, String rateName, int rate, int tax) {
        this.code = code;
        this.rateName = rateName;
        this.rate = rate;
        this.tax = tax;
    }

    public String getCode() {
        return code;
    }

    public String getRateName() {
        return rateName;
    }

    public int getRate() {
        return rate;
    }

    public int getTax() {
        return tax;
    }
}
