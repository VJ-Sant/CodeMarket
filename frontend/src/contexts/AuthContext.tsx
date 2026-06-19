import { createContext, useContext, useEffect, useState } from 'react';
import type { ReactNode } from 'react';
import { AuthResponse } from '../types';
import api from '../api/api';

interface AuthContextValue {
  user: AuthResponse | null;
  login: (email: string, password: string) => Promise<void>;
  register: (data: { username: string; email: string; phone: string; password: string }) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthResponse | null>(() => {
    const stored = localStorage.getItem('codemarket_user');
    return stored ? JSON.parse(stored) : null;
  });

  useEffect(() => {
    if (user) {
      localStorage.setItem('codemarket_user', JSON.stringify(user));
      api.defaults.headers.common.Authorization = `Bearer ${user.accessToken}`;
    } else {
      localStorage.removeItem('codemarket_user');
      delete api.defaults.headers.common.Authorization;
    }
  }, [user]);

  const login = async (email: string, password: string) => {
    const response = await api.post<AuthResponse>('/auth/login', { email, password });
    setUser(response.data);
  };

  const register = async (data: { username: string; email: string; phone: string; password: string }) => {
    const response = await api.post<AuthResponse>('/auth/register', data);
    setUser(response.data);
  };

  const logout = () => {
    setUser(null);
  };

  return <AuthContext.Provider value={{ user, login, register, logout }}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
}
