<template>
  <el-container>
    <el-header class="homeHeader">
      <div class="title">zk-web</div>
      <div>
        <el-dropdown @command="handleCommand">
          <span class="el-dropdown-link">
            集群管理<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="addZk">新增</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <el-dialog title="zk集群" :visible.sync="dialogFormVisible">
          <el-form :model="zkClusterForm">
            <el-form-item label="名称" :label-width="formLabelWidth">
              <el-input v-model="zkClusterForm.name"></el-input>
            </el-form-item>
            <el-form-item label="描述" :label-width="formLabelWidth">
              <el-input v-model="zkClusterForm.description"></el-input>
            </el-form-item>
            <el-form-item label="地址" :label-width="formLabelWidth">
              <el-input v-model="zkClusterForm.url"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="addCluster">确 定</el-button>
          </div>
        </el-dialog>
      </div>
    </el-header>
    <el-container>
      <el-aside>
        <el-menu @select="clusterSelect">
          <el-submenu index="1">
            <template slot="title">
              <i class="el-icon-s-platform"></i>集群列表
            </template>
            <el-menu-item v-for="cluster in clusters" :index="cluster.id.toString()" :key="cluster.id" style="text-align: left">
              {{ cluster.name }}
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-main>
        <el-row>
          <el-col :span="8">
            <el-tree :props="nodes" :load="loadNode" lazy @node-click="nodeSelect"></el-tree>
          </el-col>
          <el-col :span="16">
            <el-card class="box-card">
              <div>
                <el-descriptions title="集群状态">
                  <el-descriptions-item label="id">{{ curClusterStatus.id }}</el-descriptions-item>
                  <el-descriptions-item label="name">{{ curClusterStatus.name }}</el-descriptions-item>
                  <el-descriptions-item label="description">{{ curClusterStatus.description }}</el-descriptions-item>
                  <el-descriptions-item label="url">{{ curClusterStatus.url }}</el-descriptions-item>
                </el-descriptions>
                <el-table :data="curClusterStatus.status" style="width: 100%">
                  <el-table-column prop="address" label="地址" width="180"></el-table-column>
                  <el-table-column prop="role" label="角色" width="180"></el-table-column>
                  <el-table-column prop="health" label="健康度"></el-table-column>
                </el-table>
              </div>
            </el-card>
            <el-card class="box-card">
              <div>
                <el-descriptions title="节点信息">
                  <el-descriptions-item v-for="(value, name) in curNode.stat" :label="name" :key="name">
                    {{ value }}
                  </el-descriptions-item>
                </el-descriptions>
              </div>
            </el-card>
            <el-card class="box-card">
              <div slot="header" class="clearfix" style="height: 18px">
                <span style="font-size: 16px; font-weight: 700; float: left; padding: 3px 0;">节点值</span>
                <el-button style="float: right; padding: 3px 0" type="text" v-on:click="editNodeData"><i
                    class="el-icon-edit"></i></el-button>
              </div>
              <div>
                <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" v-model="curNode.value"
                          :disabled="disableEdit"></el-input>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  data() {
    return {
      search: '',
      zkClusterForm: {
        name: "",
        description: "",
        url: ""
      },
      formLabelWidth: '120px',
      dialogFormVisible: false,
      curNode: {
        stat: {
          czxid: 0,
          mzxid: 0,
          ctime: 0,
          mtime: 0,
          version: 0,
          cversion: 0,
          aversion: 0,
          ephemeralOwner: 0,
          dataLength: 3,
          numChildren: 1,
          pzxid: 214748365653
        },
        value: "",
        canEdit: true
      },
      curClusterStatus: {
        id: 1,
        name: "demo",
        description: "demo",
        url: "127.0.0.1:2181",
        status: [{
          address: "127.0.0.1:2181",
          role: "leader",
          health: "healthy"
        }]
      },
      nodes: {
        label: function (data, node) {
          if (node.level === 0 || data.path === '/') {
            return '根目录'
          }
          let paths = data.path.split('/')
          return paths[paths.length - 1]
        },
        children: 'childrenNodes',
        isLeaf: function (data, node) {
          console.log(data, node)
          if (data.stat) {
            return data.stat.numChildren === 0
          }
          return false
        }
      },
      clusters: [],
      disableEdit: true
    }
  },
  mounted() {
    this.getRequest('/zk/configs').then(resp => {
      this.clusters = resp
      if(this.clusters.length>0){
        this.getClustersStatus(this.clusters[0].id)
      }
    })
  },
  methods: {
    handleCommand(command) {
      console.log("command:" + command)
      this.dialogFormVisible = true
    },
    editNodeData() {
      this.disableEdit = !this.disableEdit
    },

    loadNode(node, resolve) {
      if (node.level === 0) {
        return resolve([{path: "/"}])
      } else {
        this.getZkNode(node.data.path, resolve)
      }
    },

    getZkNode(path, resolve) {
      this.getRequest("/zk/node?" + "zkId=" + this.curClusterStatus.id + "&path=" + path).then(resp => {
        resolve(resp.childrenNodes)
      })
    },

    getClusters() {
      this.getRequest('/zk/configs').then(resp => {
        this.clusters = resp
      })
    },

    getClustersStatus(zkId) {
      this.getRequest("/zk/status?" + "zkId=" + zkId).then(resp => {
        let clusterStatus = resp
        let statuses = []
        for (let i = 0; i < resp.status.length; i++) {
          let status = resp.status[i]
          statuses.push({
            address: status.address,
            health: status.health ? '健康' : '不健康',
            role: status.role
          })
        }
        clusterStatus.status = statuses
        this.curClusterStatus = clusterStatus
      })
    },

    nodeSelect(data) {
      this.curNode = data
    },

    clusterSelect(key) {
      for (let i = 0; i < this.clusters.length; i++) {
        if (this.clusters[i].id.toString() === key) {
          this.curClusterStatus = this.clusters[i]
          this.curClusterStatus.status = []
          this.getClustersStatus(key)
        }
      }
      this.curNode = {}
    },

    addCluster(){
      this.postRequest("/zk/configs", this.zkClusterForm).then(() => {
        this.dialogFormVisible=false
      })
    }
  }
}
</script>

<style>
.homeHeader {
  background-color: #409eff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0px 15px;
  box-sizing: border-box;
}

.homeHeader .title {
  font-size: 30px;
  font-family: 华文行楷;
  color: #ffffff
}

.homeHeader .userInfo {
  cursor: pointer;
}

.el-dropdown-link img {
  width: 48px;
  height: 48px;
  border-radius: 24px;
  margin-left: 8px;
}

.el-dropdown-link {
  display: flex;
  align-items: center;
}

</style>