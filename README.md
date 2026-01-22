# saas-ai-editor


# Database Schema
Core Entities

### 👤 USER
* Stores user identity and authentication data.
* Email-based login
* Password hashing
* Profile metadata
* Soft deletion support

### 📦 PLAN

* Defines subscription tiers and limits.
* Max projects
* Max AI tokens per day
* Max previews
* Feature flags (e.g. unlimited AI)

### 💳 SUBSCRIPTION

* Tracks a user’s active Stripe subscription.
* Stripe subscription ID
* Billing period
* Subscription status
* Used to enforce plan limits

### 📁 PROJECT

* Represents a user workspace (similar to a repo or app).
* Owned by a user
* Can be public or private
* Central tenant boundary for authorization

### 👥 PROJECT_MEMBER

* Enables team collaboration and RBAC.
* Maps users to projects
* Roles: EDITOR, VIEWER
* Tracks invitations and audit history
  #### Used to enforce:
      Who can view a project
      Who can edit files or run previews

### 📄 PROJECT_FILE

* Stores project file metadata.
* File path
* Object storage reference (MinIO)
* Created / updated by tracking
* Actual file contents are stored in object storage, not the database.

### 💬 CHAT_SESSION

* Represents an AI conversation per project.
* One project can have multiple chat sessions
* Used to group chat messages logically

### 🧾 CHAT_MESSAGE

* Stores AI and user messages.
* Role: user / assistant / system
* Supports tool calls
* Tracks token usage per message
* Immutable audit trail

### 🚀 PREVIEW

* Tracks live preview environments.
* One active preview per project
* Stores Kubernetes namespace & pod info
* Tracks lifecycle (started, terminated)
* This allows safe, isolated execution per project.

### 📊 USAGE_LOG

* Tracks platform usage for billing and analytics.
* Tokens used
* Execution duration
* Action metadata
* Used for quota enforcement and dashboards