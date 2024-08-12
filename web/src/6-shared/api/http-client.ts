export class HttpClient {
  async request(input: RequestInfo | URL, init?: RequestInit) {
    const baseUrl = window.location.origin.includes('localhost') ? 'http://localhost:3001' : '/api';

    const resp = await fetch(`${baseUrl}${input}`, {
      ...init,
      headers: {
        ...init?.headers,
        'Content-Type': 'application/json',
      },
    });

    return await resp.json();
  }
}

export const httpClient = new HttpClient();
