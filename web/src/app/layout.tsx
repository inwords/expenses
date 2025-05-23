import type {Metadata, Viewport} from 'next';
import './globals.css';
import {Roboto} from 'next/font/google';

// If loading a variable font, you don't need to specify the font weight
const roboto = Roboto({
  weight: ['300', '400', '500', '700'],
  display: 'swap',
  subsets: ['cyrillic', 'latin'],
});

export const metadata: Metadata = {
  title: 'Commonex',
  description: 'Share expenses',
};

export const viewport: Viewport = {
  initialScale: 1,
  width: 'device-width',
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" className={roboto.className}>
      <body>{children}</body>
    </html>
  );
}
