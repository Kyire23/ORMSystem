<template>
  <div>
    <div id="container" style="width: 100%; height: calc(100vh - 100px)">
      <baidu-map :center="center" :zoom="zoom" @ready="handler" style="height:1080px" @click="getClickInfo" :scroll-wheel-zoom='true'>
      </baidu-map>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Map',
  data() {
    return {}
  },
  created() {

  },
  mounted() {
    // 创建地图实例
    // 百度地图API功能
    var map = new AMap.Map('container', {
      zoom: 13
    });
  },
  methods: {}
}
</script>

<style>

</style>
