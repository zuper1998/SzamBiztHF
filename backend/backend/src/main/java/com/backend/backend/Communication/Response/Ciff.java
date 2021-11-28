package com.backend.backend.Communication.Response;
public class Ciff {

        private Integer width;

        private Integer height;

        private Integer duration;

        private int[] rgb_values;

        public Ciff(Integer width, Integer height, Integer duration, int[] rgb_values) {
            this.width = width;
            this.height = height;
            this.duration = duration;
            this.rgb_values = rgb_values;
        }


    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getDuration() {
        return duration;
    }

    public int[] getRgb_values() {
        return rgb_values;
    }
}
