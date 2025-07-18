# Git Commands Quick Reference for Auction System

## 🌟 Available Branches

| Branch | Responsible Member | Primary Focus |
|--------|-------------------|---------------|
| `feature/admin-system` | Member 1 | Administrative functions and system oversight |
| `feature/buyer-experience` | Member 2 | Buyer interface and shopping experience |
| `feature/seller-management` | Member 3 | Seller tools and product management |
| `feature/auction-system` | Member 4 | Auction mechanics and bidding system |
| `feature/database-system` | Member 5 | Data persistence and database operations |
| `feature/security-system` | Member 6 | Authentication, authorization, and security |

## 🚀 Quick Start Commands

### Initial Setup (for each team member):
```bash
# Clone the repository
git clone https://github.com/T-sashi-pavan/AUCTION.git
cd AUCTION

# Switch to your assigned branch
git checkout feature/admin-system          # Member 1
git checkout feature/buyer-experience      # Member 2
git checkout feature/seller-management     # Member 3
git checkout feature/auction-system        # Member 4
git checkout feature/database-system       # Member 5
git checkout feature/security-system       # Member 6
```

### Daily Development Workflow:
```bash
# 1. Start your day by pulling latest changes
git pull origin your-branch-name

# 2. Work on your features and make changes
# ... edit your files ...

# 3. Check what files you've changed
git status

# 4. Add your changes
git add .
# or add specific files
git add src/main/java/com/auction/services/impl/YourServiceImpl.java

# 5. Commit your changes with a descriptive message
git commit -m "Add new feature: describe what you implemented"

# 6. Push your changes to GitHub
git push origin your-branch-name
```

### Staying Updated with Main Branch:
```bash
# Switch to main branch
git checkout main

# Pull latest changes from main
git pull origin main

# Switch back to your feature branch
git checkout feature/your-branch-name

# Merge main into your branch to get latest updates
git merge main

# Push the updated branch
git push origin feature/your-branch-name
```

### Working with Multiple Files:
```bash
# Add all modified files
git add .

# Add specific files
git add src/main/java/com/auction/services/impl/AdminServiceImpl.java
git add src/main/java/com/auction/models/Admin.java

# Add all Java files
git add "*.java"

# Add files in a specific directory
git add src/main/java/com/auction/services/
```

### Commit Message Examples:
```bash
# Feature additions
git commit -m "Add user management functionality to admin dashboard"
git commit -m "Implement product search and filtering for buyers"
git commit -m "Add auction time extension feature"

# Bug fixes
git commit -m "Fix table alignment in seller dashboard"
git commit -m "Resolve sold products display issue"
git commit -m "Fix bid validation logic"

# Improvements
git commit -m "Enhance product listing performance"
git commit -m "Improve error handling in user authentication"
git commit -m "Optimize database query for auction search"
```

### Branch Management:
```bash
# List all branches
git branch -a

# Create a new branch (if needed)
git checkout -b feature/new-feature-name

# Delete a local branch (careful!)
git branch -d branch-name

# Rename current branch
git branch -m new-branch-name
```

### Viewing Changes:
```bash
# See what changes you've made
git diff

# See changes in a specific file
git diff src/main/java/com/auction/services/impl/AdminServiceImpl.java

# See commit history
git log --oneline

# See changes between branches
git diff main..feature/your-branch-name
```

### Undoing Changes:
```bash
# Undo changes to a specific file (before commit)
git checkout -- filename.java

# Undo all changes (before commit)
git checkout .

# Undo last commit (keep changes)
git reset --soft HEAD~1

# Undo last commit (discard changes) - BE CAREFUL!
git reset --hard HEAD~1
```

### Creating Pull Requests:
1. Push your branch: `git push origin feature/your-branch-name`
2. Go to: https://github.com/T-sashi-pavan/AUCTION
3. Click "Compare & pull request" for your branch
4. Add a descriptive title and description
5. Submit the pull request for review

## 🔧 Maven Commands (for Java compilation):
```bash
# Compile the project
mvn compile

# Run tests
mvn test

# Clean and compile
mvn clean compile

# Run a specific class
mvn exec:java -Dexec.mainClass="com.auction.AuctionSystemApplication"
```

## 🆘 Troubleshooting

### Common Issues and Solutions:

#### "Your branch is behind"
```bash
git pull origin your-branch-name
```

#### "Merge conflicts"
1. Open the conflicted files
2. Look for `<<<<<<<`, `=======`, `>>>>>>>`
3. Edit the file to resolve conflicts
4. Remove the conflict markers
5. Add and commit the resolved files:
```bash
git add .
git commit -m "Resolve merge conflicts"
```

#### "Permission denied" or authentication issues
- Make sure you have access to the repository
- Use your GitHub credentials when prompted
- Consider using a personal access token instead of password

#### "Files not staging"
```bash
# Check git status
git status

# Force add if needed
git add -f filename.java
```

## 📋 Best Practices

1. **Commit often**: Make small, frequent commits
2. **Descriptive messages**: Write clear commit messages
3. **Pull regularly**: Stay updated with main branch
4. **Test before push**: Make sure your code compiles and runs
5. **Backup your work**: Push regularly to avoid losing work

## 🎯 Team Coordination

- **Daily standup**: Share what you're working on
- **Code review**: Review each other's pull requests
- **Documentation**: Update documentation when adding features
- **Testing**: Test your features thoroughly
- **Communication**: Use GitHub issues or discussions for coordination

---

**Need help?** Ask your team members or check the main documentation in `BRANCH_ORGANIZATION.md`
