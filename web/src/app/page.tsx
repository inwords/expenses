'use client';

import {Route, Routes} from 'react-router';
import {BrowserRouter} from 'react-router-dom';
import {MainPage} from '@/pages/MainPage';
import {SecondPage} from '@/pages/SecondPage';
import {NoSsr} from '@mui/material';
import {ROUTES} from '@/shared/routing/constants';
import {EventPage} from '@/pages/EventPage';

export default function Home() {
  return (
    <NoSsr>
      <BrowserRouter>
        <Routes>
          <Route path={'/'} element={<MainPage />} />

          <Route path={'/second'} element={<SecondPage />} />

          <Route path={ROUTES.Event(':id')} element={<EventPage />} />
        </Routes>
      </BrowserRouter>
    </NoSsr>
  );
}
