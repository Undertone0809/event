package com.event.feign;

import com.event.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("user")
public interface UserFeign {
    @GetMapping("/user/hasCollectionUser/{collectionUserId}/{beCollectionUserId}")
    Result<Boolean> hasCollectionUser(@PathVariable Integer collectionUserId, @PathVariable Integer beCollectionUserId);

    @GetMapping("/user/selectCollectionUserNum/{collectionUserId}")
    Result<Integer> selectCollectionUserNum(@PathVariable("collectionUserId")Integer collectionUserId);

    @GetMapping("/user/beSelectCollectionUserNum/{beCollectionUserId}")
    Result<Integer> beSelectCollectionUserNum(@PathVariable("beCollectionUserId")Integer beCollectionUserId);
}
