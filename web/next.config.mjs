/** @type {import('next').NextConfig} */
const nextConfig = {
    output: "export",
    trailingSlash: true,
    distDir: 'build',
    rewrites: createRewrites(),
};

export default nextConfig;

function createRewrites() {
    return () => ({
        fallback: [
            {
                source: '/:path*',
                destination: `/`,
            },
        ],
    });
}