Backend Enhancement Suggestions
To support the "Mailnest" style modern application features, the following backend enhancements are recommended:

1. User Profile & Social Features
Current Gap: The API allows authentication but lacks endpoints for users to manage their public profile (Avatar, Display Name) beyond the initial admin creation.

Proposed Endpoints:

GET /me: Return current authenticated user's full profile (including Avatar URL, Bio/Status).
PATCH /me: Allow updating Display Name, Avatar URL, and Status.
GET /users/{id}/avatar: Serving user avatars directly or proxying Gravatar.
2. User Preferences & Settings
Current Gap: Theme (Dark/Light) and App Settings (Signatures, Notification rules) are currently client-side only and wont sync across devices.

Proposed Endpoints:

GET /me/settings: Fetch synced preferences (Theme mode, Notification settings, Signature).
PATCH /me/settings: Update preferences.
3. Folder & Label Management
Current Gap: GET /messages supports filtering by folder, but there is no explicit API to Create/Rename/Delete custom folders or Labels.

Proposed Endpoints:

GET /folders: List all user folders/labels with counts.
POST /folders: Create new folder/label.
PATCH /folders/{id}: Rename/Move folder.
DELETE /folders/{id}: Delete folder.
4. Rich Text / HTML Sanitization
Suggestion: Ensure the backend explicitly handles input sanitization for rich text emails to prevent XSS, as the new UI might render more complex HTML content.