package com.luci.cvgenerator.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "email")
	private String email;

	@Column(name = "enabled")
	private int enabled;

	@Column(name = "suspended")
	private int suspended;

	@Column(name = "email_token")
	private String emailToken;

	@Column(name = "password_token")
	private String passwordToken;

	@Column(name = "confirmation_token")
	private String confirmationToken;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_detail_id")
	private UserDetail userDetail;

	@ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH,
			CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Collection<Role> roles;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<Education> educationList;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<HardSkill> hardSkillList;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<Language> languageList;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<Project> projectList;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<SoftSkill> softSkillList;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<Experience> experienceList;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<Interest> interestList;

	public User() {
	}

	public User(String username, String password, String email, int enabled, String emailToken, String passwordToken,
			String confirmationToken, UserDetail userDetail, Collection<Role> roles, int suspended) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.enabled = enabled;
		this.emailToken = emailToken;
		this.passwordToken = passwordToken;
		this.confirmationToken = confirmationToken;
		this.userDetail = userDetail;
		this.roles = roles;
		this.suspended = suspended;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public String getEmailToken() {
		return emailToken;
	}

	public void setEmailToken(String emailToken) {
		this.emailToken = emailToken;
	}

	public String getPasswordToken() {
		return passwordToken;
	}

	public void setPasswordToken(String passwordToken) {
		this.passwordToken = passwordToken;
	}

	public String getConfirmationToken() {
		return confirmationToken;
	}

	public void setConfirmationToken(String confirmationToken) {
		this.confirmationToken = confirmationToken;
	}

	public int getSuspended() {
		return suspended;
	}

	public void setSuspended(int suspended) {
		this.suspended = suspended;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserDetail getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	public List<Education> getEducationList() {
		return educationList;
	}

	public void setEducationList(List<Education> educationList) {
		this.educationList = educationList;
	}

	public List<HardSkill> getHardSkillList() {
		return hardSkillList;
	}

	public void setHardSkillList(List<HardSkill> hardSkillList) {
		this.hardSkillList = hardSkillList;
	}

	public List<Language> getLanguageList() {
		return languageList;
	}

	public void setLanguageList(List<Language> languageList) {
		this.languageList = languageList;
	}

	public List<Project> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<Project> projectList) {
		this.projectList = projectList;
	}

	public List<SoftSkill> getSoftSkillList() {
		return softSkillList;
	}

	public void setSoftSkillList(List<SoftSkill> softSkillList) {
		this.softSkillList = softSkillList;
	}

	public List<Experience> getExperienceList() {
		return experienceList;
	}

	public void setExperienceList(List<Experience> experienceList) {
		this.experienceList = experienceList;
	}

	public List<Interest> getInterestList() {
		return interestList;
	}

	public void setInterestList(List<Interest> interestList) {
		this.interestList = interestList;
	}

	public void addInterest(Interest interest) {
		if (interestList == null) {
			interestList = new ArrayList<>();
		}

		for (int i = 0; i < interestList.size(); i++) {
			if (interestList.get(i).getId() == interest.getId()) {
				interestList.set(i, interest);
				return;
			}
		}

		interestList.add(interest);

	}

	public void removeInterest(int index) {
		interestList.remove(index);
	}

	public void addEducation(Education education) {
		if (educationList == null) {
			educationList = new ArrayList<>();
		}

		for (int i = 0; i < educationList.size(); i++) {
			if (educationList.get(i).getId() == education.getId()) {
				educationList.set(i, education);
				return;
			}
		}

		educationList.add(education);

	}

	public void removeEducation(int index) {
		educationList.remove(index);
	}

	public void addLanguage(Language language) {
		if (languageList == null) {
			languageList = new ArrayList<>();
		}

		for (int i = 0; i < languageList.size(); i++) {
			if (languageList.get(i).getId() == language.getId()) {
				languageList.set(i, language);
				return;
			}
		}

		languageList.add(language);

	}

	public void addExperience(Experience experience) {
		if (experienceList == null) {
			experienceList = new ArrayList<>();
		}

		for (int i = 0; i < experienceList.size(); i++) {
			if (experienceList.get(i).getId() == experience.getId()) {
				experienceList.set(i, experience);
				return;
			}
		}

		experienceList.add(experience);

	}

	public void addHardSkill(HardSkill hardSkill) {
		if (hardSkillList == null) {
			hardSkillList = new ArrayList<>();
		}

		for (int i = 0; i < hardSkillList.size(); i++) {
			if (hardSkillList.get(i).getId() == hardSkill.getId()) {
				hardSkillList.set(i, hardSkill);
				return;
			}
		}

		hardSkillList.add(hardSkill);

	}

	public void addSoftSkill(SoftSkill softSkill) {
		if (softSkillList == null) {
			softSkillList = new ArrayList<>();
		}

		for (int i = 0; i < softSkillList.size(); i++) {
			if (softSkillList.get(i).getId() == softSkill.getId()) {
				softSkillList.set(i, softSkill);
				return;
			}
		}

		softSkillList.add(softSkill);

	}

	public void addProject(Project project) {
		if (projectList == null) {
			projectList = new ArrayList<>();
		}

		for (int i = 0; i < projectList.size(); i++) {
			if (projectList.get(i).getId() == project.getId()) {
				projectList.set(i, project);
				return;
			}
		}

		projectList.add(project);

	}

	public void removeExperience(int index) {
		experienceList.remove(index);
	}

	public void removeLanguage(int index) {
		languageList.remove(index);
	}

	public void removeHardSkill(int index) {
		hardSkillList.remove(index);
	}

	public void removeSoftSkill(int index) {
		softSkillList.remove(index);
	}

	public void removeProject(int index) {
		projectList.remove(index);
	}

}
