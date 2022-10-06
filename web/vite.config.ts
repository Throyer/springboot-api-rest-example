import react from '@vitejs/plugin-react';
import { defineConfig, loadEnv } from 'vite';

export default ({ mode }) => {
  process.env = {...process.env, ...loadEnv(mode, process.cwd())};
  return defineConfig({
    plugins: [react()],
    server: {
      port: Number(process.env.APP_PORT),
    },
  });
}