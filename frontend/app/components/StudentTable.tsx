"use client";

import React, { useState, useEffect, useCallback } from "react";
import { Student } from "../types";
import { studentApi } from "../services/api";
import {
  Search,
  Plus,
  Edit,
  Trash2,
  RefreshCw,
  Loader2,
  AlertCircle,
  X,
  Mail,
} from "lucide-react";

export default function StudentTable({
  onStatsUpdate,
}: {
  onStatsUpdate?: (count: number) => void;
}) {
  const [students, setStudents] = useState<Student[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [keyword, setKeyword] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingStudent, setEditingStudent] = useState<Student | null>(null);
  const [formData, setFormData] = useState<Omit<Student, "id">>({
    name: "",
    email: "",
  });
  const [submitting, setSubmitting] = useState(false);
  const [deletingId, setDeletingId] = useState<number | null>(null);

  const fetchStudents = useCallback(async (searchQuery?: string) => {
    try {
      setLoading(true);
      setError(null);
      const data = await studentApi.getAll(searchQuery);
      setStudents(data);
      if (onStatsUpdate) onStatsUpdate(data.length);
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Không thể tải danh sách sinh viên");
    } finally {
      setLoading(false);
    }
  }, [onStatsUpdate]);

  useEffect(() => {
    let ignore = false;
    async function loadData() {
      try {
        const data = await studentApi.getAll(keyword);
        if (!ignore) {
          setStudents(data);
          onStatsUpdate?.(data.length);
        }
      } catch (err: unknown) {
        if (!ignore) {
          setError(err instanceof Error ? err.message : "Không thể tải danh sách sinh viên");
        }
      } finally {
        if (!ignore) {
          setLoading(false);
        }
      }
    }

    loadData();
    return () => {
      ignore = true;
    };
  }, [keyword, onStatsUpdate]);

  const handleOpenCreateModal = () => {
    setEditingStudent(null);
    setFormData({
      name: "",
      email: "",
    });
    setIsModalOpen(true);
  };

  const handleOpenEditModal = (student: Student) => {
    setEditingStudent(student);
    setFormData({
      name: student.name,
      email: student.email,
    });
    setIsModalOpen(true);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!formData.name.trim() || !formData.email.trim()) {
      alert("Vui lòng nhập họ tên và email");
      return;
    }

    try {
      setSubmitting(true);
      if (editingStudent && editingStudent.id) {
        await studentApi.update(editingStudent.id, {
          id: editingStudent.id,
          ...formData,
        });
      } else {
        await studentApi.create(formData);
      }
      setIsModalOpen(false);
      fetchStudents(keyword);
    } catch (err: unknown) {
      alert(err instanceof Error ? err.message : "Có lỗi xảy ra khi lưu sinh viên");
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm("Bạn có chắc chắn muốn xóa sinh viên này?")) return;
    try {
      setDeletingId(id);
      await studentApi.delete(id);
      fetchStudents(keyword);
    } catch (err: unknown) {
      alert(err instanceof Error ? err.message : "Không thể xóa sinh viên");
    } finally {
      setDeletingId(null);
    }
  };

  return (
    <div className="bg-white rounded-xl shadow-sm border border-slate-200 overflow-hidden">
      {/* Header & Actions */}
      <div className="p-5 border-b border-slate-100 flex flex-col sm:flex-row sm:items-center justify-between gap-4 bg-slate-50/50">
        <div>
          <h2 className="text-lg font-bold text-slate-800 flex items-center gap-2">
            Danh Sách Sinh Viên
            <span className="text-xs font-semibold px-2 py-0.5 rounded-full bg-emerald-100 text-emerald-700">
              {students.length}
            </span>
          </h2>
          <p className="text-xs text-slate-500 mt-0.5">Dữ liệu lấy trực tiếp từ Backend API: <code>/api/students</code></p>
        </div>

        <div className="flex items-center gap-3">
          <div className="relative">
            <Search className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              type="text"
              placeholder="Tìm theo tên, email..."
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
              className="pl-9 pr-4 py-2 text-sm bg-white border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent w-full sm:w-64 transition-all"
            />
          </div>

          <button
            onClick={() => fetchStudents(keyword)}
            disabled={loading}
            className="p-2 text-slate-600 hover:text-emerald-600 hover:bg-emerald-50 border border-slate-200 rounded-lg transition-colors cursor-pointer"
            title="Tải lại dữ liệu"
          >
            <RefreshCw className={`w-4 h-4 ${loading ? "animate-spin text-emerald-600" : ""}`} />
          </button>

          <button
            onClick={handleOpenCreateModal}
            className="flex items-center gap-1.5 px-3.5 py-2 bg-emerald-600 hover:bg-emerald-700 text-white text-sm font-medium rounded-lg shadow-sm transition-all hover:shadow cursor-pointer"
          >
            <Plus className="w-4 h-4" />
            Thêm Sinh Viên
          </button>
        </div>
      </div>

      {/* Error state */}
      {error && (
        <div className="p-4 m-4 rounded-lg bg-red-50 border border-red-200 text-red-700 flex items-center justify-between text-sm">
          <div className="flex items-center gap-2">
            <AlertCircle className="w-5 h-5 flex-shrink-0" />
            <span>{error}</span>
          </div>
          <button
            onClick={() => fetchStudents(keyword)}
            className="underline font-medium hover:text-red-800 text-xs cursor-pointer"
          >
            Thử lại
          </button>
        </div>
      )}

      {/* Table */}
      <div className="overflow-x-auto">
        <table className="w-full text-left text-sm border-collapse">
          <thead>
            <tr className="border-b border-slate-200 bg-slate-50 text-slate-600 text-xs font-semibold uppercase tracking-wider">
              <th className="py-3 px-4 w-16">ID</th>
              <th className="py-3 px-4">Họ Và Tên</th>
              <th className="py-3 px-4">Email</th>
              <th className="py-3 px-4 text-right w-28">Thao Tác</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-100 text-slate-700">
            {loading ? (
              <tr>
                <td colSpan={4} className="py-12 text-center text-slate-400">
                  <Loader2 className="w-6 h-6 animate-spin mx-auto mb-2 text-emerald-600" />
                  Đang tải dữ liệu sinh viên từ Backend...
                </td>
              </tr>
            ) : students.length === 0 ? (
              <tr>
                <td colSpan={4} className="py-12 text-center text-slate-400">
                  <div className="text-base font-medium text-slate-600 mb-1">Chưa có sinh viên nào</div>
                  <p className="text-xs">Hãy nhấn nút &quot;Thêm Sinh Viên&quot; để lưu sinh viên mới vào CSDL.</p>
                </td>
              </tr>
            ) : (
              students.map((student) => (
                <tr key={student.id} className="hover:bg-slate-50/80 transition-colors">
                  <td className="py-3 px-4 font-mono text-xs text-slate-500">{student.id}</td>
                  <td className="py-3 px-4 font-semibold text-slate-900">{student.name}</td>
                  <td className="py-3 px-4 text-slate-600">
                    <span className="inline-flex items-center gap-1.5">
                      <Mail className="w-3.5 h-3.5 text-slate-400" />
                      {student.email}
                    </span>
                  </td>
                  <td className="py-3 px-4 text-right">
                    <div className="flex items-center justify-end gap-1">
                      <button
                        onClick={() => handleOpenEditModal(student)}
                        className="p-1.5 text-slate-500 hover:text-emerald-600 hover:bg-emerald-50 rounded transition-colors cursor-pointer"
                        title="Chỉnh sửa"
                      >
                        <Edit className="w-4 h-4" />
                      </button>
                      <button
                        onClick={() => student.id && handleDelete(student.id)}
                        disabled={deletingId === student.id}
                        className="p-1.5 text-slate-500 hover:text-red-600 hover:bg-red-50 rounded transition-colors cursor-pointer"
                        title="Xóa"
                      >
                        {deletingId === student.id ? (
                          <Loader2 className="w-4 h-4 animate-spin text-red-600" />
                        ) : (
                          <Trash2 className="w-4 h-4" />
                        )}
                      </button>
                    </div>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {/* Modal Thêm / Sửa Sinh Viên */}
      {isModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-sm p-4">
          <div className="bg-white rounded-xl shadow-xl border border-slate-200 w-full max-w-md overflow-hidden animate-in fade-in zoom-in-95 duration-150">
            <div className="px-6 py-4 border-b border-slate-100 flex items-center justify-between">
              <h3 className="font-bold text-slate-800 text-base">
                {editingStudent ? "Chỉnh Sửa Sinh Viên" : "Thêm Sinh Viên Mới"}
              </h3>
              <button
                onClick={() => setIsModalOpen(false)}
                className="text-slate-400 hover:text-slate-600 p-1 rounded-lg cursor-pointer"
              >
                <X className="w-5 h-5" />
              </button>
            </div>

            <form onSubmit={handleSubmit} className="p-6 space-y-4">
              <div>
                <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
                  Họ Và Tên <span className="text-red-500">*</span>
                </label>
                <input
                  type="text"
                  required
                  placeholder="Ví dụ: Nguyễn Văn A..."
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-emerald-500"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
                  Email Sinh Viên <span className="text-red-500">*</span>
                </label>
                <input
                  type="email"
                  required
                  placeholder="Ví dụ: sinhvien@elearning.vn..."
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-emerald-500"
                />
              </div>

              <div className="flex items-center justify-end gap-3 pt-4 border-t border-slate-100">
                <button
                  type="button"
                  onClick={() => setIsModalOpen(false)}
                  className="px-4 py-2 text-sm font-medium text-slate-600 hover:bg-slate-100 rounded-lg transition-colors cursor-pointer"
                >
                  Hủy
                </button>
                <button
                  type="submit"
                  disabled={submitting}
                  className="flex items-center gap-1.5 px-4 py-2 bg-emerald-600 hover:bg-emerald-700 text-white text-sm font-medium rounded-lg shadow-sm transition-all cursor-pointer"
                >
                  {submitting && <Loader2 className="w-4 h-4 animate-spin" />}
                  {editingStudent ? "Cập Nhật" : "Tạo Mới"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
