export interface AuthResponse {
  accessToken: string;
  tokenType: string;
  username: string;
  role: string;
}

export interface UserProfile {
  id: number;
  username: string;
  email: string;
  phone: string;
  gender: string | null;
  role: string;
  enabled: boolean;
}

export interface User {
  id: number;
  username: string;
  email: string;
  phone: string;
  role: string;
  enabled: boolean;
}

export interface Category {
  id: number;
  name: string;
  description: string;
}

export interface Project {
  projectId: number;
  projectName: string;
  projectDescription: string;
  technologyStack: string;
  projectPrice: number;
  thumbnailUrl: string;
  difficultyLevel: string;
  createdAt: string;
  updatedAt: string;
  categoryName: string;
  categoryId: number;
  ownerUsername: string;
  ownerId: number;
}

export interface Purchase {
  id: number;
  buyerUsername: string;
  projectId: number;
  projectName: string;
  amount: number;
  purchaseDate: string;
}

export interface Review {
  id: number;
  projectId: number;
  reviewerUsername: string;
  rating: number;
  comment: string;
  reviewDate: string;
}
