<template>
  <div class="create-post">
    <div class="create-card">
      <h2>{{ isEdit ? '编辑帖子' : '发布新帖' }}</h2>

      <el-form ref="formRef" :model="form" :rules="rules" @submit.prevent="handleSubmit">
        <el-form-item prop="content">
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="8"
            placeholder="分享你的AI学习心得、经验或资源..."
            maxlength="5000"
            show-word-limit
          />
        </el-form-item>

        <el-form-item prop="resourceUrl">
          <el-input
            v-model="form.resourceUrl"
            placeholder="资源链接（可选）：https://..."
            :prefix-icon="Link"
          />
        </el-form-item>

        <el-form-item prop="tags">
          <el-input
            v-model="form.tags"
            placeholder="标签（用逗号分隔）：llm, prompt, python"
          />
        </el-form-item>

        <div class="form-actions">
          <el-button @click="router.back()">取消</el-button>
          <el-button type="primary" :loading="loading" @click="handleSubmit">
            {{ isEdit ? '保存' : '发布' }}
          </el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { usePostStore } from '@/stores/post'
import { ElMessage } from 'element-plus'
import { Link } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const postStore = usePostStore()

const formRef = ref()
const loading = ref(false)
const postId = computed(() => route.query.id)
const isEdit = computed(() => !!postId.value)

const form = reactive({
  content: '',
  resourceUrl: '',
  tags: ''
})

const rules = {
  content: [
    { required: true, message: '请输入帖子内容', trigger: 'blur' }
  ]
}

onMounted(async () => {
  if (isEdit.value) {
    const result = await postStore.getPost(postId.value)
    if (result.success) {
      const post = result.data
      form.content = post.content
      form.resourceUrl = post.resourceUrl || ''
      form.tags = post.tags || ''
    }
  }
})

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    let result
    if (isEdit.value) {
      result = await postStore.updatePost(postId.value, form)
    } else {
      result = await postStore.createPost(form)
    }

    if (result.success) {
      ElMessage.success(isEdit.value ? '保存成功' : '发布成功')
      router.push('/')
    } else {
      ElMessage.error(result.message)
    }
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.create-post {
  max-width: 680px;
  margin: 0 auto;
}

.create-card {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
}

.create-card h2 {
  margin-bottom: 24px;
  color: #333;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 16px;
}
</style>