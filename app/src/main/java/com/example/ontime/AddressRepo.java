package com.example.ontime;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AddressRepo {
    @SerializedName("meta")
    Meta meta;

    @SerializedName("documents")
    List<documents> documents = new ArrayList<>();

    public class Meta {
        @SerializedName("is_end") boolean is_end;
        @SerializedName("total_count") int total_count;
        @SerializedName("pageable_count") int pageable_count;

        public boolean isIs_end() {
            return is_end;
        }

        public int getTotal_count() {
            return total_count;
        }

        public int getPageable_count() {
            return pageable_count;
        }
    }



    public class documents{
        @SerializedName("address_name") String address_name;
        @SerializedName("x") String x;
        @SerializedName("y") String y;

        public String getAddress_name() {
            return address_name;
        }

        public String getX() {
            return x;
        }

        public String getY() {
            return y;
        }
    }

    public List<AddressRepo.documents> getDocuments() {
        return documents;
    }

    public Meta getMeta() {
        return meta;
    }
}
