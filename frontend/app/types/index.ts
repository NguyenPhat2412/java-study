export interface Student {
    id: number;
    name: string;
    email: string;
}

export interface Course {
    id: number;
    department: string;
    student: Student | null;
    favourite: string;
    isStatus: boolean;
}