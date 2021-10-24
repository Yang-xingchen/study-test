<template>
    <div>
        {{name}}
        <div v-for="d in dataList" :key="d">
            {{ d }}
        </div>
    </div>
</template>

<script>
import axios from "axios";
export default {
    name: "page1",
    data() {
        return {
            name: 'page1',
            dataList: []
        }
    },
    mounted() {
        var that = this
        axios.get('/api/datalist').then(function(res) {
            that.dataList = res.data;
        })
    }
}
</script>