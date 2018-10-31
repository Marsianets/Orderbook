package com.marsianets.orderbook;

public class TopCoat {
    private String carMaker; //Производитель автомобиля
    private String colorCode; //Код автоэмали
    private String colorGroup; //Цветовая группа: Серый, Желтый, зеленый и т.д.
    private String colorSystem; //Производитель эмали: SpiesHecker, RockPaint, Retan, Mobihel, Vika
    private String colorType; //Акрилл белый, акрилл цветной, металлик, 3-х слойный перламутр

    public TopCoat() {
        this.setCarMaker("");
        this.setColorCode("");
        this.setColorType("");
    }

    public String getCarMaker() {
        return carMaker;
    }

    public String getColorCode() {
        return colorCode;
    }

    public String getColorType() {
        return colorType;
    }

    public void setCarMaker(String carMaker) {
        this.carMaker = carMaker;
    }


    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public void setColorType(String colorType) {
        this.colorType = colorType;
    }
}
