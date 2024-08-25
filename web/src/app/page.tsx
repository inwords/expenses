'use client';

import {Navigate, Route, Routes} from 'react-router';
import {BrowserRouter} from 'react-router-dom';
import {MainPage} from '@/2-pages/MainPage';
import {NoSsr} from '@mui/material';
import {ROUTES} from '@/6-shared/routing/constants';
import {EventPage} from '@/2-pages/EventPage';

export default function Home() {
  return (
    <NoSsr>
      <BrowserRouter>
        <Routes>
          <Route path={ROUTES.Main} element={<MainPage />} />

          <Route path={ROUTES.Event(':id')} element={<EventPage />} />

          <Route path="*" element={<Navigate to={ROUTES.Main} />} />
        </Routes>
      </BrowserRouter>
    </NoSsr>
  );
}
