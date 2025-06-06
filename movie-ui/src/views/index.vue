<template>
  <div class="app-container home">
    <!-- 项目介绍 -->
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <div slot="header"><span>🎬 项目介绍</span></div>
          <p>本系统为电影网站管理平台，基于 Spring Boot + MyBatis Plus + Redis 实现，支持：</p>
          <ul>
            <li>会员注册与登录，VIP权限控制</li>
            <li>影片按类型、地区、热度展示</li>
            <li>电影排行榜、主创检索</li>
            <li>支付宝沙箱支付、Excel报表导出</li>
          </ul>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第二行 -->
    <el-row :gutter="20" class="mt20">
      <el-col :span="12">
        <el-card>
          <div slot="header"><span>🖥️ 主机系统状态</span></div>
          <el-descriptions column="1" border class="descriptions-full-height" style="margin-top: 3vh;">
            <el-descriptions-item label="CPU 核心数">{{ hardware.cpuCores }}</el-descriptions-item>
            <el-descriptions-item label="GPU 信息">{{ hardware.gpu }}</el-descriptions-item>
            <el-descriptions-item label="内存使用">
              {{ server.mem.used }} / {{ server.mem.total }} GB
            </el-descriptions-item>
            <el-descriptions-item label="电池电量">
              {{ hardware.batteryLevel }}%
              <span v-if="hardware.isCharging !== null">
                （{{ hardware.isCharging ? '充电中' : '未充电' }}）
              </span>
            </el-descriptions-item>
            <el-descriptions-item label="系统运行时长">{{ server.jvm.runTime }}</el-descriptions-item>
            <el-descriptions-item label="网络状态">{{ hardware.networkType }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card>
          <template #header>
            <span>{{ currentChart === 'genre' ? '🎞️ 每类型电影数量' : '📍 地区电影占比' }}</span>
          </template>
          <div class="chart-wrapper">
            <div ref="genreChart" class="chart-fade" :class="{ show: currentChart === 'genre' }"></div>
            <div ref="areaChart" class="chart-fade" :class="{ show: currentChart === 'area' }"></div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from "echarts";
import request from '@/utils/request';
import { getServer } from "@/api/monitor/server";
import _ from "lodash";

export default {
  name: "Index",
  data() {
    return {
      currentChart: 'genre',
      switchInterval: null,
      genreChartInstance: null,
      areaChartInstance: null,
      hardware: {
        cpuCores: navigator.hardwareConcurrency || 0,
        gpu: "未知",
        batteryLevel: null,
        isCharging: null,
        networkType: "未知"
      },
      server: {
        mem: { used: 0, total: 0 },
        jvm: { runTime: '0天' }
      },
    };
  },
  mounted() {
    this.openLoading();
    this.getList();
    setTimeout(() => {
      this.detectGPU();
      this.detectBattery();
      this.detectNetwork();
      this.loadGenreChart();
      this.loadAreaChart();
    }, 100);

    this.debouncedResize = _.debounce(this.resizeChart, 200);
    window.addEventListener('resize', this.debouncedResize);

    this.switchInterval = setInterval(() => {
      this.currentChart = this.currentChart === 'genre' ? 'area' : 'genre';
    }, 10000);
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.debouncedResize);
    clearInterval(this.switchInterval);
    if (this.genreChartInstance) {
      this.genreChartInstance.dispose();
    }
    if (this.areaChartInstance) {
      this.areaChartInstance.dispose();
    }
  },
  methods: {
    openLoading() {
      this.$modal.loading("正在加载数据，请稍候！");
    },
    getList() {
      getServer().then(response => {
        this.server = response.data;
        this.$modal.closeLoading();
      });
    },
    detectGPU() {
      try {
        const canvas = document.createElement("canvas");
        const gl = canvas.getContext("webgl") || canvas.getContext("experimental-webgl");
        const debugInfo = gl.getExtension("WEBGL_debug_renderer_info");
        const gpuInfo = debugInfo
          ? gl.getParameter(debugInfo.UNMASKED_RENDERER_WEBGL)
          : "无法检测";
        const match = gpuInfo.match(/(NVIDIA|AMD|Intel)[^,()]+/i);
        this.hardware.gpu = match ? match[0].trim() : gpuInfo.slice(0, 50);
      } catch {
        this.hardware.gpu = "不可用";
      }
    },
    detectBattery() {
      if (navigator.getBattery) {
        navigator.getBattery().then(battery => {
          this.hardware.batteryLevel = (battery.level * 100).toFixed(0);
          this.hardware.isCharging = battery.charging;
        });
      }
    },
    detectNetwork() {
      const conn = navigator.connection || navigator.webkitConnection || navigator.mozConnection;
      if (conn && conn.effectiveType) {
        this.hardware.networkType = conn.effectiveType.toUpperCase();
      }
    },
    loadGenreChart() {
      if (!this.$refs.genreChart) return;
      this.genreChartInstance = echarts.init(this.$refs.genreChart);
      request.get("/genres/genres/list").then(res => {
        if (!res?.rows) return;
        this.genreChartInstance.setOption({
          title: { text: "类型电影数量", left: "center" },
          tooltip: {},
          xAxis: {
            type: 'category',
            data: res.rows.map(item => item.type)
          },
          yAxis: { type: 'value' },
          series: [{
            data: res.rows.map(item => item.nums),
            type: 'bar',
            barWidth: '60%',
            itemStyle: { color: '#409EFF' }
          }]
        });
      });
    },
    loadAreaChart() {
      if (!this.$refs.areaChart) return;
      this.areaChartInstance = echarts.init(this.$refs.areaChart);
      request.get("/areas/areas/list").then(res => {
        if (!res?.rows) return;
        this.areaChartInstance.setOption({
          title: { text: "地区电影占比", left: "center" },
          tooltip: { trigger: 'item' },
          legend: { orient: 'vertical', left: 'left' },
          series: [{
            name: '地区占比',
            type: 'pie',
            radius: '70%',
            center: ['50%', '55%'],
            data: res.rows.map(item => ({
              value: item.novienum,
              name: item.areaname
            })),
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }]
        });
      });
    },
    resizeChart() {
      this.genreChartInstance?.resize();
      this.areaChartInstance?.resize();
    }
  }
};
</script>

<style scoped>
.app-container.home {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 86px);
  padding: 16px;
  box-sizing: border-box;
}

.mt20 {
  flex: 1;
  display: flex !important;
  gap: 20px;
}

.mt20 > .el-col > .el-card {
  display: block;
  height: 95%;
}
.mt20 > .el-col > .el-card__body {
  height: calc(100% - 57px);
  position: relative;
}

.descriptions-full-height {
  flex: 1;
  overflow-y: auto;
}
.descriptions-full-height ::v-deep tr.el-descriptions-row {
  height: 70px !important;
}

.chart-wrapper {
  position: relative;
  height: 400px;
  margin-top: 4vh;
}

.chart-fade {
  transition: opacity 0.5s ease;
  opacity: 0;
  height: 100%;
  width: 100%;
  position: absolute;
  top: 0;
  left: 0;
}
.chart-fade.show {
  opacity: 1;
  z-index: 1;
}
</style>
