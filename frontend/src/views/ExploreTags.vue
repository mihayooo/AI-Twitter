<template>
  <div class="explore-tags">
    <div class="header">
      <h2>热门标签</h2>
      <p class="subtitle">发现最受关注的AI学习话题</p>
    </div>

    <div v-loading="loading" class="tags-container">
      <el-empty v-if="!loading && tags.length === 0" description="暂无标签数据" />

      <div v-else class="tags-list">
        <div
          v-for="(tagStat, index) in tags"
          :key="tagStat.tag"
          class="tag-item"
          @click="goToTagPosts(tagStat.tag)"
        >
          <div class="rank-number">{{ index + 1 }}</div>
          <div class="tag-info">
            <div class="tag-name">#{{ tagStat.tag }}</div>
            <div class="tag-count">{{ tagStat.count }} 个帖子</div>
          </div>
          <div class="tag-action">
            <el-button type="primary" size="small" round>查看</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getHotTags } from '@/api/posts'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const tags = ref([])

onMounted(() => {
  loadHotTags()
})

async function loadHotTags() {
  loading.value = true
  try {
    const res = await getHotTags(50)
    if (res.success) {
      tags.value = res.data
    }
  } catch (error) {
    ElMessage.error('加载标签失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

function goToTagPosts(tag) {
  router.push({
    name: 'Explore',
    query: { tag }
  })
}
</script>

<style scoped>
.explore-tags {
  max-width: 800px;
  margin: 0 auto;
  padding: 24px;
}

.header {
  text-align: center;
  margin-bottom: 40px;
  padding: 40px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  color: white;
}

.header h2 {
  font-size: 28px;
  margin: 0 0 8px 0;
  font-weight: 600;
}

.subtitle {
  font-size: 14px;
  opacity: 0.9;
  margin: 0;
}

.tags-container {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.tags-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.tag-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 20px;
  background: #f8f9fa;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.tag-item:hover {
  background: #e8eaf6;
  border-color: #667eea;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
}

.rank-number {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 16px;
  flex-shrink: 0;
}

.tag-info {
  flex: 1;
}

.tag-name {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  margin-bottom: 4px;
}

.tag-count {
  font-size: 13px;
  color: #999;
}

.tag-action {
  flex-shrink: 0;
}
</style>
