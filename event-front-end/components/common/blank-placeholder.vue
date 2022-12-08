<template>

  <div class="blank-placeholder" :style="{height:height_+'rpx'}"></div>
</template>
<script>
  export default{
    props:{
      height_:{
        type:Number,
        default: 200
      }
    }
  }
</script>

<style>
.blank-placeholder{
  position: relative;
  height: 200rpx;
  width: 1rpx;
}
</style>