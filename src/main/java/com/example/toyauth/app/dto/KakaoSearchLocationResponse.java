package com.example.toyauth.app.dto;


import java.util.List;

public record KakaoSearchLocationResponse(
        List<Document> documents,
        Meta meta
) {
    public record Document(
            String address_name,
            String category_group_code,
            String category_group_name,
            String category_name,
            String distance,
            String id,
            String phone,
            String place_name,
            String place_url,
            String road_address_name,
            String x,
            String y
    ) { }

    public record Meta(
            boolean is_end,
            int pageable_count,
            int total_count,
            SameName same_name
    ) {
        public record SameName(
                String keyword,
                String selected_region,
                String[] region
        ) {}
    }
}

