"use client";

import React, { useState, useEffect, useCallback } from "react";
import { Course } from "../types";
import { courseApi } from "../services/api";
import {
  Search,
  Plus,
  Edit,
  Trash2,
  RefreshCw,
  Loader2,
  AlertCircle,
  CheckCircle,
  X,
} from "lucide-react";

export default function CourseTable({
  onStatsUpdate,
}: {
  onStatsUpdate?: (count: number) => void;
}) {
  const [courses, setCourses] = useState<Course[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [keyword, setKeyword] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingCourse, setEditingCourse] = useState<Course | null>(null);
  const [formData, setFormData] = useState<Omit<Course, "id">>({
    department: "",
    student: "",
    favourite: "",
    isStatus: true,
  });
  const [submitting, setSubmitting] = useState(false);
  const [deletingId, setDeletingId] = useState<number | null>(null);

  const fetchCourses = useCallback(async (searchQuery?: string) => {
    try {
      setLoading(true);
      setError(null);
      const data = await courseApi.getAll(searchQuery);
      setCourses(data);
      if (onStatsUpdate) onStatsUpdate(data.length);
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Không thể tải danh sách khóa học");
    } finally {
      setLoading(false);
    }
  }, [onStatsUpdate]);

  useEffect(() => {
    let ignore = false;
    async function loadData() {
      try {
        const data = await courseApi.getAll(keyword);
        if (!ignore) {
          setCourses(data);
          onStatsUpdate?.(data.length);
        }
      } catch (err: unknown) {
        if (!ignore) {
          setError(err instanceof Error ? err.message : "Không thể tải danh sách khóa học");
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
    setEditingCourse(null);
    setFormData({
      department: "",
      student: "",
      favourite: "",
      isStatus: true,
    });
    setIsModalOpen(true);
  };

  const handleOpenEditModal = (course: Course) => {
    setEditingCourse(course);
    setFormData({
      department: course.department,
      student: course.student,
      favourite: course.favourite || "",
      isStatus: course.isStatus !== false,
    });
    setIsModalOpen(true);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!formData.department.trim() || !formData.student.trim()) {
      alert("Vui lòng điền đầy đủ Khoa và Tên sinh viên");
      return;
    }

    try {
      setSubmitting(true);
      if (editingCourse && editingCourse.id) {
        await courseApi.update(editingCourse.id, {
          id: editingCourse.id,
          ...formData,
        });
      } else {
        await courseApi.create(formData);
      }
      setIsModalOpen(false);
      fetchCourses(keyword);
    } catch (err: unknown) {
      alert(err instanceof Error ? err.message : "Có lỗi xảy ra khi lưu khóa học");
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm("Bạn có chắc chắn muốn xóa khóa học này?")) return;
    try {
      setDeletingId(id);
      await courseApi.delete(id);
      fetchCourses(keyword);
    } catch (err: unknown) {
      alert(err instanceof Error ? err.message : "Không thể xóa khóa học");
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
            Danh Sách Khóa Học
            <span className="text-xs font-semibold px-2 py-0.5 rounded-full bg-blue-100 text-blue-700">
              {courses.length}
            </span>
          </h2>
          <p className="text-xs text-slate-500 mt-0.5">Dữ liệu lấy trực tiếp từ Backend API: <code>/api/courses</code></p>
        </div>

        <div className="flex items-center gap-3">
          <div className="relative">
            <Search className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              type="text"
              placeholder="Tìm theo khoa, tên, sở thích..."
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
              className="pl-9 pr-4 py-2 text-sm bg-white border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent w-full sm:w-64 transition-all"
            />
          </div>

          <button
            onClick={() => fetchCourses(keyword)}
            disabled={loading}
            className="p-2 text-slate-600 hover:text-blue-600 hover:bg-blue-50 border border-slate-200 rounded-lg transition-colors cursor-pointer"
            title="Tải lại dữ liệu"
          >
            <RefreshCw className={`w-4 h-4 ${loading ? "animate-spin text-blue-600" : ""}`} />
          </button>

          <button
            onClick={handleOpenCreateModal}
            className="flex items-center gap-1.5 px-3.5 py-2 bg-blue-600 hover:bg-blue-700 text-white text-sm font-medium rounded-lg shadow-sm transition-all hover:shadow cursor-pointer"
          >
            <Plus className="w-4 h-4" />
            Thêm Khóa Học
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
            onClick={() => fetchCourses(keyword)}
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
              <th className="py-3 px-4">Khoa / Bộ Môn</th>
              <th className="py-3 px-4">Sinh Viên</th>
              <th className="py-3 px-4">Sở Thích / Đề Tài</th>
              <th className="py-3 px-4 w-32">Trạng Thái</th>
              <th className="py-3 px-4 text-right w-28">Thao Tác</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-100 text-slate-700">
            {loading ? (
              <tr>
                <td colSpan={6} className="py-12 text-center text-slate-400">
                  <Loader2 className="w-6 h-6 animate-spin mx-auto mb-2 text-blue-600" />
                  Đang tải dữ liệu từ Backend...
                </td>
              </tr>
            ) : courses.length === 0 ? (
              <tr>
                <td colSpan={6} className="py-12 text-center text-slate-400">
                  <div className="text-base font-medium text-slate-600 mb-1">Chưa có khóa học nào</div>
                  <p className="text-xs">Hãy nhấn nút &quot;Thêm Khóa Học&quot; để tạo bản ghi đầu tiên vào CSDL.</p>
                </td>
              </tr>
            ) : (
              courses.map((course) => (
                <tr key={course.id} className="hover:bg-slate-50/80 transition-colors">
                  <td className="py-3 px-4 font-mono text-xs text-slate-500">{course.id}</td>
                  <td className="py-3 px-4 font-medium text-slate-900">{course.department}</td>
                  <td className="py-3 px-4 text-slate-800">{course.student}</td>
                  <td className="py-3 px-4 text-slate-600">
                    {course.favourite || <span className="text-slate-400 italic">Không có</span>}
                  </td>
                  <td className="py-3 px-4">
                    {course.isStatus !== false ? (
                      <span className="inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-xs font-medium bg-emerald-50 text-emerald-700 border border-emerald-200">
                        <CheckCircle className="w-3 h-3" /> Hoạt động
                      </span>
                    ) : (
                      <span className="inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-xs font-medium bg-slate-100 text-slate-600 border border-slate-200">
                        <X className="w-3 h-3" /> Đã đóng
                      </span>
                    )}
                  </td>
                  <td className="py-3 px-4 text-right">
                    <div className="flex items-center justify-end gap-1">
                      <button
                        onClick={() => handleOpenEditModal(course)}
                        className="p-1.5 text-slate-500 hover:text-blue-600 hover:bg-blue-50 rounded transition-colors cursor-pointer"
                        title="Chỉnh sửa"
                      >
                        <Edit className="w-4 h-4" />
                      </button>
                      <button
                        onClick={() => course.id && handleDelete(course.id)}
                        disabled={deletingId === course.id}
                        className="p-1.5 text-slate-500 hover:text-red-600 hover:bg-red-50 rounded transition-colors cursor-pointer"
                        title="Xóa"
                      >
                        {deletingId === course.id ? (
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

      {/* Modal Thêm / Sửa */}
      {isModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-sm p-4">
          <div className="bg-white rounded-xl shadow-xl border border-slate-200 w-full max-w-md overflow-hidden animate-in fade-in zoom-in-95 duration-150">
            <div className="px-6 py-4 border-b border-slate-100 flex items-center justify-between">
              <h3 className="font-bold text-slate-800 text-base">
                {editingCourse ? "Chỉnh Sửa Khóa Học" : "Thêm Khóa Học Mới"}
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
                  Khoa / Bộ Môn <span className="text-red-500">*</span>
                </label>
                <input
                  type="text"
                  required
                  placeholder="Ví dụ: Công nghệ thông tin, Điện tử..."
                  value={formData.department}
                  onChange={(e) => setFormData({ ...formData, department: e.target.value })}
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
                  Tên Sinh Viên <span className="text-red-500">*</span>
                </label>
                <input
                  type="text"
                  required
                  placeholder="Ví dụ: Nguyễn Văn A..."
                  value={formData.student}
                  onChange={(e) => setFormData({ ...formData, student: e.target.value })}
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
                  Sở Thích / Đề Tài
                </label>
                <input
                  type="text"
                  placeholder="Ví dụ: AI, Web Development, Robotics..."
                  value={formData.favourite}
                  onChange={(e) => setFormData({ ...formData, favourite: e.target.value })}
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>

              <div className="flex items-center gap-2 pt-1">
                <input
                  type="checkbox"
                  id="courseStatus"
                  checked={formData.isStatus !== false}
                  onChange={(e) => setFormData({ ...formData, isStatus: e.target.checked })}
                  className="w-4 h-4 text-blue-600 rounded border-slate-300 focus:ring-blue-500"
                />
                <label htmlFor="courseStatus" className="text-sm text-slate-700 font-medium cursor-pointer">
                  Đang hoạt động (Active)
                </label>
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
                  className="flex items-center gap-1.5 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white text-sm font-medium rounded-lg shadow-sm transition-all cursor-pointer"
                >
                  {submitting && <Loader2 className="w-4 h-4 animate-spin" />}
                  {editingCourse ? "Cập Nhật" : "Tạo Mới"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
