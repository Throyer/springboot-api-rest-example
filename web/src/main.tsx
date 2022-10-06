import { createRoot } from 'react-dom/client';

import { BrowserRouter } from 'react-router-dom';
import { App } from './App';
import { Providers } from './providers';

const element = document.getElementById('root') as Element;
const root = createRoot(element);

root.render(
  <BrowserRouter>
    <Providers>
      <App />
    </Providers>
  </BrowserRouter>
);
