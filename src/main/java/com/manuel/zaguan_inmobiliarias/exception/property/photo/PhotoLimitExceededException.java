package com.manuel.zaguan_inmobiliarias.exception.property.photo;

public class PhotoLimitExceededException extends RuntimeException {

    public PhotoLimitExceededException(int maxPhotos){
        super("Maximum number of photos reached: " + maxPhotos);
    }
}
