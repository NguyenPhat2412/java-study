import { Course, Student } from "../types";

const BASE_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080/api";

async function request<T>(endpoint: string, options?: RequestInit): Promise<T> {
  const url = `${BASE_URL}${endpoint}`;
  try {
    const res = await fetch(url, {
      headers: {
        "Content-Type": "application/json",
      },
      ...options,
    });

    if (!res.ok) {
      const errorBody = await res.json().catch(() => null);
      const message = errorBody?.message || errorBody?.error || `HTTP error: ${res.status}`;
      throw new Error(message);
    }

    if (res.status === 204) {
      return null as T;
    }

    return await res.json();
  } catch (error: unknown) {
    if (error instanceof TypeError && error.message.includes("fetch")) {
      throw new Error(`Không thể kết nối đến Backend API (${BASE_URL}). Vui lòng đảm bảo Spring Boot đang chạy.`);
    }
    throw error;
  }
}

export const courseApi = {
  getAll: (keyword?: string) => {
    const query = keyword ? `?keyword=${encodeURIComponent(keyword)}` : "";
    return request<Course[]>(`/courses${query}`);
  },
  getById: (id: number) => request<Course>(`/courses/${id}`),
  create: (data: Omit<Course, "id">) =>
    request<Course>("/courses", {
      method: "POST",
      body: JSON.stringify(data),
    }),
  update: (id: number, data: Course) =>
    request<Course>(`/courses/${id}`, {
      method: "PUT",
      body: JSON.stringify(data),
    }),
  patch: (id: number, data: Partial<Course>) =>
    request<Course>(`/courses/${id}`, {
      method: "PATCH",
      body: JSON.stringify(data),
    }),
  delete: (id: number) =>
    request<void>(`/courses/${id}`, {
      method: "DELETE",
    }),
};

export const studentApi = {
  getAll: (keyword?: string) => {
    const query = keyword ? `?keyword=${encodeURIComponent(keyword)}` : "";
    return request<Student[]>(`/students${query}`);
  },
  getById: (id: number) => request<Student>(`/students/${id}`),
  create: (data: Omit<Student, "id">) =>
    request<Student>("/students", {
      method: "POST",
      body: JSON.stringify(data),
    }),
  update: (id: number, data: Student) =>
    request<Student>(`/students/${id}`, {
      method: "PUT",
      body: JSON.stringify(data),
    }),
  delete: (id: number) =>
    request<void>(`/students/${id}`, {
      method: "DELETE",
    }),
};
