import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  define: {
    'process.env': {}
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    port: 1024,
    hmr: true,
    host: '0.0.0.0',
    proxy: {
      "/api": {
        target: "http://123.60.171.78:7090",
        changeOrigin: true,
        pathRewrite: {
          "^api": "/api"
        }
      }
    }
  },
  build: {
    chunkSizeWarningLimit: 3000,
    rollupOptions: {
      output: {
        manualChunks(id) {
          if (id.includes('node_modules')) {
            return id.toString().split('node_modules/')[1].split('/')[0].toString();
          }
        }
      }
    },
    chunkFileNames: (chunkInfo) => {
      const facadeModuleId = chunkInfo.facadeModuleId
        ? chunkInfo.facadeModuleId.split('/')
        : [];
      const fileName =
        facadeModuleId[facadeModuleId.length - 2] || '[name]';
      return `js/${fileName}/[name].[hash].js`;
    }
  }
})
