package com.pend.arena;

import java.util.List;

/**
 * Created by chaudhary on 3/17/2018.
 */

public class MetaDataResponse extends Data {

    List<metaData> metaData;

    public List<MetaDataResponse.metaData> getMetaData() {
        return metaData;
    }

    public void setMetaData(List<MetaDataResponse.metaData> metaData) {
        this.metaData = metaData;
    }

    class metaData
    {
        String keyID;
        String value;
        String lastModifiedDatetime;

        public String getKeyID() {
            return keyID;
        }

        public void setKeyID(String keyID) {
            this.keyID = keyID;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getLastModifiedDatetime() {
            return lastModifiedDatetime;
        }

        public void setLastModifiedDatetime(String lastModifiedDatetime) {
            this.lastModifiedDatetime = lastModifiedDatetime;
        }
    }


}
