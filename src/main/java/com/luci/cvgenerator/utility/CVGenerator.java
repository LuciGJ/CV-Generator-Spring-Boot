package com.luci.cvgenerator.utility;

import java.awt.Color;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import com.luci.cvgenerator.entity.Education;
import com.luci.cvgenerator.entity.Experience;
import com.luci.cvgenerator.entity.HardSkill;
import com.luci.cvgenerator.entity.Interest;
import com.luci.cvgenerator.entity.Language;
import com.luci.cvgenerator.entity.Project;
import com.luci.cvgenerator.entity.SoftSkill;
import com.luci.cvgenerator.entity.User;
import com.luci.cvgenerator.entity.UserDetail;
import com.luci.cvgenerator.service.UserService;

public class CVGenerator {

	private static List<String> getLinesLong(String text, int fontSize, PDType1Font font, float width)
			throws IOException {
		List<String> lines = new ArrayList<String>();
		int lastSpace = -1;
		while (text.length() > 0) {
			int spaceIndex = text.indexOf(' ', lastSpace + 1);
			if (spaceIndex < 0)
				spaceIndex = text.length();
			String subString = text.substring(0, spaceIndex);
			float size = fontSize * font.getStringWidth(subString) / 1000;
			if (size > width - 100) {
				if (lastSpace < 0)
					lastSpace = spaceIndex;
				subString = text.substring(0, lastSpace);
				lines.add(subString);
				text = text.substring(lastSpace).trim();
				lastSpace = -1;
			} else if (spaceIndex == text.length()) {
				lines.add(text);
				text = "";
			} else {
				lastSpace = spaceIndex;
			}
		}

		return lines;
	}

	private static List<String> getLines(String text, int fontSize, PDType1Font font, float width) throws IOException {
		List<String> lines = new ArrayList<String>();
		int lastSpace = -1;
		while (text.length() > 0) {
			int spaceIndex = text.indexOf(' ', lastSpace + 1);
			if (spaceIndex < 0)
				spaceIndex = text.length();
			String subString = text.substring(0, spaceIndex);
			float size = fontSize * font.getStringWidth(subString) / 1000;
			if (size > width + 50) {
				if (lastSpace < 0)
					lastSpace = spaceIndex;
				subString = text.substring(0, lastSpace);
				lines.add(subString);
				text = text.substring(lastSpace).trim();
				lastSpace = -1;
			} else if (spaceIndex == text.length()) {
				lines.add(text);
				text = "";
			} else {
				lastSpace = spaceIndex;
			}
		}

		return lines;
	}

	private static float getWidth(String string, PDType1Font font, int fontSize) throws IOException {
		return font.getStringWidth(string) / 1000 * fontSize;
	}
	
	


	public static void generateCVPDF(UserService userService, Principal principal, CV cv) {

		User user = userService.findUserByUsername(principal.getName());

		UserDetail userDetail = user.getUserDetail();

		List<Education> educationList = user.getEducationList();

		List<Experience> experienceList = user.getExperienceList();

		List<HardSkill> hardSkillList = user.getHardSkillList();

		List<Language> languageList = user.getLanguageList();

		List<Project> projectList = user.getProjectList();

		List<SoftSkill> softSkillList = user.getSoftSkillList();

		List<Interest> interestList = user.getInterestList();

		if (cv.getDescription() == null) {
			cv.setDescription("Description");
		}

		if (cv.getEducation() == null) {
			cv.setEducation("Education");
		}

		if (cv.getExperience() == null) {
			cv.setExperience("Experience");
		}

		if (cv.getHardSkills() == null) {
			cv.setHardSkills("Hard skills");
		}

		if (cv.getHobbies() == null) {
			cv.setHobbies("Hobbies");
		}

		if (cv.getLanguages() == null) {
			cv.setLanguages("Languages");
		}

		if (cv.getProjects() == null) {
			cv.setProjects("Projects");
		}

		if (cv.getSoftSkills() == null) {
			cv.setSoftSkills("Soft skills");
		}

		Color color;

		switch (cv.getFontColor()) {
		case "Black":
			color = Color.BLACK;
			break;
		case "Blue":
			color = Color.BLUE;
			break;
		case "Dark Gray":
		default:
			color = Color.DARK_GRAY;
			break;
		}

		PDType1Font font;

		PDType1Font sectionFont;

		int fontSize = 16;

		int sectionFontSize = 24;

		switch (cv.getFontType()) {
		case "Times New Roman":
			font = PDType1Font.TIMES_ROMAN;
			sectionFont = PDType1Font.TIMES_BOLD;
			break;
		case "Helvetica":
		default:
			font = PDType1Font.HELVETICA;
			sectionFont = PDType1Font.HELVETICA_BOLD;
			break;
		}

		boolean isCentered;

		if (cv.getCenter().equals("Center")) {
			isCentered = true;
		} else {
			isCentered = false;
		}

		PDDocument document = new PDDocument();

		try {

			PDDocumentInformation information = document.getDocumentInformation();

			if (userDetail.getFirstName() != null && userDetail.getLastName() != null) {
				information.setAuthor(userDetail.getFirstName() + " " + userDetail.getLastName());
			} else {
				information.setAuthor("Unknown");
			}

			information.setTitle("CV");

			information.setCreator("CV Generator");

			Calendar date = new GregorianCalendar();

			date.setTimeInMillis(System.currentTimeMillis());

			information.setCreationDate(date);

			PDPage page = new PDPage();

			document.addPage(page);

			PDPageContentStream contentStream = new PDPageContentStream(document, page);

			contentStream.moveTo(0, 0);

			contentStream.setNonStrokingColor(color);
			contentStream.setStrokingColor(color);

			float x = page.getCropBox().getLowerLeftX() + 30f;
			float y = page.getCropBox().getUpperRightY() - 30f;
			float tempX = x;
			float tempY = y;

			float startX = page.getCropBox().getLowerLeftX() + 30f;
			float endX = page.getCropBox().getUpperRightX() - 30f;
			float leading = 20f;
			PDRectangle mediabox = page.getMediaBox();
			float margin = 72f;
			float width = mediabox.getWidth() - 2f * margin;

			float center = tempX;

			if (isCentered) {
				center = width / 2;
			}

			if (cv.isDisplayPicture()) {
				if (!userDetail.getImage().equals("default.png")) {
					PDImageXObject pdImage = PDImageXObject.createFromFile("user-photos/" + userDetail.getImage(),
							document);
					contentStream.addRect(center, 600, 150, 150);
					contentStream.drawImage(pdImage, center, 600, 150, 150);
					contentStream.stroke();
					tempY -= 200f;
				}
			}

			contentStream.setFont(font, fontSize);

			boolean addedName = false;

			if (userDetail.getFirstName() != null || userDetail.getLastName() != null) {
				contentStream.beginText();
				String theName = "";
				if (userDetail.getFirstName() != null) {
					theName += userDetail.getFirstName();
				}

				if (userDetail.getLastName() != null) {
					theName += " ";
					theName += userDetail.getLastName();
				}

					if (isCentered) {
						contentStream.newLineAtOffset(center - getWidth(theName, sectionFont, 20) / 2f + margin, tempY) ;
					} else {
						contentStream.newLineAtOffset(tempX, tempY);
					}
				

				if (userDetail.getFirstName() != null) {
					contentStream.setFont(sectionFont, 20);
					contentStream.showText(userDetail.getFirstName());
					addedName = true;
				}

				if (userDetail.getLastName() != null) {
					contentStream.setFont(sectionFont, 20);
					String lastName = (userDetail.getFirstName() != null) ? " " + userDetail.getLastName()
							: userDetail.getLastName();
					contentStream.showText(lastName);
					addedName = true;
				}
				if (addedName) {
					tempY -= 20;
				}
				contentStream.endText();
			}

			if (cv.isDisplayDetails()) {
				contentStream.beginText();
				contentStream.setFont(font, 10);

				StringBuilder newLine = new StringBuilder("");

				if (userDetail.getEmail() != null) {
					newLine.append(userDetail.getEmail());
				}

				if (userDetail.getPhone() != null) {

					if (!newLine.toString().equals("")) {
						newLine.append(" / ");
					}

					newLine.append(userDetail.getPhone());
				}

				if (userDetail.getAddress() != null) {

					if (!newLine.toString().equals("")) {
						newLine.append(" / ");
					}

					newLine.append(userDetail.getAddress());

				}

				if (userDetail.getBirthday() != null) {

					if (!newLine.toString().equals("")) {
						newLine.append(" / ");
					}

					newLine.append(userDetail.getBirthday());
				}

				if (isCentered) {
					contentStream.newLineAtOffset(center - getWidth(newLine.toString(), font, 10) / 2f + margin, tempY);
				} else {
					contentStream.newLineAtOffset(tempX, tempY);
				}
				
				if (!newLine.toString().equals("")) {
					contentStream.showText(newLine.toString());
					tempY -= 10;
				}
				contentStream.endText();
			}

			if (addedName) {
				contentStream.setLineWidth(5f);
				contentStream.moveTo(startX, tempY);
				contentStream.lineTo(endX, tempY);
				contentStream.stroke();

				tempY -= leading;
				tempY -= 10;

			}

			contentStream.setLineWidth(2.5f);

			boolean firstSection = false;

			if (cv.isDisplayDescription()) {
				if (userDetail.getDescription() != null) {
					firstSection = true;
					contentStream.beginText();
					contentStream.newLineAtOffset(center, tempY);
					contentStream.setFont(sectionFont, sectionFontSize);
					contentStream.showText(cv.getDescription());
					contentStream.setFont(font, fontSize);
					tempY -= leading;
					String description = userDetail.getDescription();
					List<String> lines = getLines(description, fontSize, font, width);
					if (lines.size() > 0) {
						contentStream.newLineAtOffset(-center + tempX, -sectionFontSize);
						tempY -= sectionFontSize;
						tempY += leading;
					}
					for (String line : lines) {
						contentStream.showText(line);
						contentStream.newLineAtOffset(0, -leading);
						tempY -= leading;
					}
					contentStream.endText();
				}

			}

			if (cv.isDisplayEducation()) {
				if (educationList != null) {
					contentStream.beginText();
					if (firstSection) {
						contentStream.endText();
						contentStream.moveTo(startX, tempY);
						contentStream.lineTo(endX, tempY);
						contentStream.stroke();
						tempY -= leading;
						tempY -= 10;
						contentStream.beginText();
					} else {
						firstSection = true;
					}
					contentStream.setFont(sectionFont, sectionFontSize);
					contentStream.newLineAtOffset(center, tempY);
					contentStream.setFont(sectionFont, sectionFontSize);
					contentStream.showText(cv.getEducation());
					contentStream.setFont(font, fontSize);
					contentStream.newLineAtOffset(-center + tempX, -sectionFontSize);
					tempY -= leading;

					for (Education education : educationList) {

						if (education.getName() != null) {
							StringBuilder builder = new StringBuilder("");
							builder.append(education.getName());
							if (education.getInstitution() != null) {
								builder.append(", " + education.getInstitution());
							}
							if (education.getStarted() != null) {
								builder.append(": " + education.getStarted());
								if (education.getGraduated() != null) {
									builder.append(" - " + education.getGraduated());
								}
							}
							contentStream.showText(builder.toString());

							if (tempY - leading - 20 > 0) {
								contentStream.newLineAtOffset(0, -leading);
								tempY -= leading;
							} else {
								contentStream.endText();
								contentStream.close();
								page = new PDPage();
								document.addPage(page);

								contentStream = new PDPageContentStream(document, page);
								contentStream.setFont(font, fontSize);
								contentStream.setNonStrokingColor(color);
								contentStream.setStrokingColor(color);
								contentStream.setLineWidth(2.5f);
								contentStream.beginText();
								tempY = y;
								contentStream.newLineAtOffset(tempX, tempY);
							}

						}
					}
					contentStream.endText();
				}
			}

			if (cv.isDisplayProjects()) {
				if (projectList != null) {
					contentStream.beginText();
					if (firstSection) {
						contentStream.endText();
						contentStream.moveTo(startX, tempY);
						contentStream.lineTo(endX, tempY);
						contentStream.stroke();
						tempY -= leading;
						tempY -= 10;
						contentStream.beginText();
					} else {
						firstSection = true;
					}
					contentStream.setFont(sectionFont, sectionFontSize);
					contentStream.newLineAtOffset(center, tempY);
					contentStream.setFont(sectionFont, sectionFontSize);
					contentStream.showText(cv.getProjects());
					contentStream.setFont(font, fontSize);
					contentStream.newLineAtOffset(-center + tempX, -sectionFontSize);
					tempY -= leading;

					for (Project project : projectList) {

						if (project.getName() != null) {
							contentStream.showText(project.getName());
							if (project.getDescription() == null) {
								project.setDescription(" ");
							}
							contentStream.newLineAtOffset(getWidth(project.getName(), font, fontSize), 0);
							if (!project.getDescription().equals(" ")) {
								contentStream.showText(": ");
							}
							String description = project.getDescription();
							List<String> lines;
							if (project.getName().length() > 20) {
								lines = getLinesLong(description, fontSize, font, width);
							} else {
								lines = getLines(description, fontSize, font, width);
							}
							int i = 0;
							for (String line : lines) {

								if (tempY - leading > -20 || i == 0) {
									contentStream.showText(line);
									contentStream.newLineAtOffset(getWidth(": ", font, fontSize), -leading);
									tempY -= leading;
									if (i++ != 0) {
										contentStream.newLineAtOffset(-getWidth(": ", font, fontSize), 0);
									}

								} else {
									if (i == 0) {
										contentStream.showText(line);
									}
									contentStream.endText();
									contentStream.close();
									page = new PDPage();
									document.addPage(page);

									contentStream = new PDPageContentStream(document, page);
									contentStream.setNonStrokingColor(color);
									contentStream.setStrokingColor(color);
									contentStream.setLineWidth(2.5f);
									contentStream.setFont(font, fontSize);
									contentStream.beginText();
									tempY = y;
									contentStream.newLineAtOffset(getWidth(project.getName(), font, fontSize) + tempX
											+ getWidth(": ", font, fontSize), tempY);
									if (i != 0) {
										contentStream.showText(line);
									} else {
										i++;
									}
									contentStream.newLineAtOffset(0, -leading);
									tempY -= leading;
								}

							}
							contentStream.newLineAtOffset(
									-getWidth(project.getName(), font, fontSize) - getWidth(": ", font, fontSize), 0);

						}
					}
					contentStream.endText();
				}
			}

			if (cv.isDisplayExperience()) {
				if (experienceList != null) {
					contentStream.beginText();
					if (firstSection) {
						contentStream.endText();
						contentStream.moveTo(startX, tempY);
						contentStream.lineTo(endX, tempY);
						contentStream.stroke();
						tempY -= leading;
						tempY -= 10;
						contentStream.beginText();
					} else {
						firstSection = true;
					}
					contentStream.setFont(sectionFont, sectionFontSize);
					contentStream.newLineAtOffset(center, tempY);
					contentStream.setFont(sectionFont, sectionFontSize);
					contentStream.showText(cv.getExperience());
					contentStream.setFont(font, fontSize);
					contentStream.newLineAtOffset(-center + tempX, -sectionFontSize);
					tempY -= leading;

					for (Experience experience : experienceList) {

						if (experience.getRole() != null) {
							StringBuilder builder = new StringBuilder("");
							builder.append(experience.getRole());
							if (experience.getName() != null) {
								builder.append(", " + experience.getName());
							}
							if (experience.getStartDate() != null) {
								builder.append(", " + experience.getStartDate());
								if (experience.getEndDate() != null) {
									builder.append(" - " + experience.getEndDate());
								}
							}
							contentStream.showText(builder.toString());

							if (experience.getDescription() == null) {
								experience.setDescription(" ");
							}
							contentStream.newLineAtOffset(getWidth(builder.toString(), font, fontSize), 0);
							if (!experience.getDescription().equals(" ")) {
								contentStream.showText(": ");
							}
							String description = experience.getDescription();
							List<String> lines;
							if (builder.toString().length() > 20) {
								lines = getLinesLong(description, fontSize, font, width);
							} else {
								lines = getLines(description, fontSize, font, width);
							}
							int i = 0;
							for (String line : lines) {
								if (tempY - leading - 20 > 0) {
									contentStream.showText(line);
									contentStream.newLineAtOffset(getWidth(": ", font, fontSize), -leading);
									tempY -= leading;
									if (i++ != 0) {
										contentStream.newLineAtOffset(-getWidth(": ", font, fontSize), 0);
									}

								} else {
									if (i == 0) {
										contentStream.showText(line);
									}
									contentStream.endText();
									contentStream.close();
									page = new PDPage();
									document.addPage(page);

									contentStream = new PDPageContentStream(document, page);
									contentStream.setNonStrokingColor(color);
									contentStream.setStrokingColor(color);
									contentStream.setFont(font, fontSize);
									contentStream.setLineWidth(2.5f);
									contentStream.beginText();
									tempY = y;
									contentStream.newLineAtOffset(getWidth(experience.getName(), font, fontSize) + tempX
											+ getWidth(": ", font, fontSize), tempY);
									if (i != 0) {
										contentStream.showText(line);
									} else {
										i++;
									}
									contentStream.newLineAtOffset(0, -leading);
									tempY -= leading;
								}

							}
							contentStream.newLineAtOffset(
									-getWidth(builder.toString(), font, fontSize) - getWidth(": ", font, fontSize), 0);

						}
					}
					contentStream.endText();
				}
			}

			if (cv.isDisplayLanguages()) {
				if (languageList != null) {
					contentStream.beginText();
					if (firstSection) {
						contentStream.endText();
						contentStream.moveTo(startX, tempY);
						contentStream.lineTo(endX, tempY);
						contentStream.stroke();
						tempY -= leading;
						tempY -= 10;
						contentStream.beginText();
					} else {
						firstSection = true;
					}
					contentStream.setFont(sectionFont, sectionFontSize);
					contentStream.newLineAtOffset(center, tempY);
					contentStream.setFont(sectionFont, sectionFontSize);
					contentStream.showText(cv.getLanguages());
					contentStream.setFont(font, fontSize);
					contentStream.newLineAtOffset(-center + tempX, -sectionFontSize);
					tempY -= leading;

					for (Language language : languageList) {

						if (language.getName() != null) {
							StringBuilder builder = new StringBuilder("");
							builder.append(language.getName());
							if (language.getLevel() != null) {
								builder.append(": " + language.getLevel());
							}

							contentStream.showText(builder.toString());

							if (tempY - leading - 20 > 0) {
								contentStream.newLineAtOffset(0, -leading);
								tempY -= leading;
							} else {
								contentStream.endText();
								contentStream.close();
								page = new PDPage();
								document.addPage(page);

								contentStream = new PDPageContentStream(document, page);
								contentStream.setFont(font, fontSize);
								contentStream.setNonStrokingColor(color);
								contentStream.setStrokingColor(color);
								contentStream.setLineWidth(2.5f);
								contentStream.beginText();
								tempY = y;
								contentStream.newLineAtOffset(tempX, tempY);
							}

						}
					}
					contentStream.endText();
				}
			}

			if (cv.isDisplayHardSkill()) {

				if (hardSkillList != null) {
					contentStream.beginText();
					if (firstSection) {
						contentStream.endText();
						contentStream.moveTo(startX, tempY);
						contentStream.lineTo(endX, tempY);
						contentStream.stroke();
						tempY -= leading;
						tempY -= 10;
						contentStream.beginText();
					} else {
						firstSection = true;
					}
					contentStream.setFont(sectionFont, sectionFontSize);
					contentStream.newLineAtOffset(center, tempY);
					contentStream.setFont(sectionFont, sectionFontSize);
					contentStream.showText(cv.getHardSkills());
					contentStream.setFont(font, fontSize);
					contentStream.newLineAtOffset(-center + tempX, -sectionFontSize);
					tempY -= leading;

					for (HardSkill hardSkill : hardSkillList) {

						if (hardSkill.getName() != null) {
							contentStream.showText(hardSkill.getName());
							if (hardSkill.getDescription() == null) {
								hardSkill.setDescription(" ");
							}
							contentStream.newLineAtOffset(getWidth(hardSkill.getName(), font, fontSize), 0);
							if (!hardSkill.getDescription().equals(" ")) {
								contentStream.showText(": ");
							}
							String description = hardSkill.getDescription();
							List<String> lines;
							if (hardSkill.getName().length() > 20) {
								lines = getLinesLong(description, fontSize, font, width);
							} else {
								lines = getLines(description, fontSize, font, width);
							}
							int i = 0;
							for (String line : lines) {
								if (tempY - leading - 20 > 0) {
									contentStream.showText(line);
									contentStream.newLineAtOffset(getWidth(": ", font, fontSize), -leading);
									tempY -= leading;
									if (i++ != 0) {
										contentStream.newLineAtOffset(-getWidth(": ", font, fontSize), 0);
									}

								} else {
									if (i == 0) {
										contentStream.showText(line);
									}
									contentStream.endText();
									contentStream.close();
									page = new PDPage();
									document.addPage(page);

									contentStream = new PDPageContentStream(document, page);
									contentStream.setNonStrokingColor(color);
									contentStream.setStrokingColor(color);
									contentStream.setLineWidth(2.5f);
									contentStream.setFont(font, fontSize);
									contentStream.beginText();
									tempY = y;
									contentStream.newLineAtOffset(getWidth(hardSkill.getName(), font, fontSize) + tempX
											+ getWidth(": ", font, fontSize), tempY);
									if (i != 0) {
										contentStream.showText(line);
									} else {
										i++;
									}
									contentStream.newLineAtOffset(0, -leading);
									tempY -= leading;
								}

							}
							contentStream.newLineAtOffset(
									-getWidth(hardSkill.getName(), font, fontSize) - getWidth(": ", font, fontSize), 0);

						}
					}
					contentStream.endText();
				}
			}

			if (cv.isDisplaySoftSkill()) {
				if (softSkillList != null) {
					contentStream.beginText();
					if (firstSection) {
						contentStream.endText();
						contentStream.moveTo(startX, tempY);
						contentStream.lineTo(endX, tempY);
						contentStream.stroke();
						tempY -= leading;
						tempY -= 10;
						contentStream.beginText();
					} else {
						firstSection = true;
					}
					contentStream.setFont(sectionFont, sectionFontSize);
					contentStream.newLineAtOffset(center, tempY);
					contentStream.setFont(sectionFont, sectionFontSize);
					contentStream.showText(cv.getSoftSkills());
					contentStream.setFont(font, fontSize);
					contentStream.newLineAtOffset(-center + tempX, -sectionFontSize);
					tempY -= leading;

					for (SoftSkill softSkill : softSkillList) {

						if (softSkill.getName() != null) {
							contentStream.showText(softSkill.getName());
							if (softSkill.getDescription() == null) {
								softSkill.setDescription(" ");
							}
							contentStream.newLineAtOffset(getWidth(softSkill.getName(), font, fontSize), 0);
							if (!softSkill.getDescription().equals(" ")) {
								contentStream.showText(": ");
							}
							String description = softSkill.getDescription();
							List<String> lines;
							if (softSkill.getName().length() > 20) {
								lines = getLinesLong(description, fontSize, font, width);
							} else {
								lines = getLines(description, fontSize, font, width);
							}
							int i = 0;
							for (String line : lines) {

								if (tempY - leading - 20 > 0) {
									contentStream.showText(line);
									contentStream.newLineAtOffset(getWidth(": ", font, fontSize), -leading);
									tempY -= leading;
									if (i++ != 0) {
										contentStream.newLineAtOffset(-getWidth(": ", font, fontSize), 0);
									}

								} else {
									if (i == 0) {
										contentStream.showText(line);
									}
									contentStream.endText();
									contentStream.close();
									page = new PDPage();
									document.addPage(page);

									contentStream = new PDPageContentStream(document, page);
									contentStream.setNonStrokingColor(color);
									contentStream.setStrokingColor(color);
									contentStream.setLineWidth(2.5f);
									contentStream.setFont(font, fontSize);
									contentStream.beginText();
									tempY = y;
									contentStream.newLineAtOffset(getWidth(softSkill.getName(), font, fontSize) + tempX
											+ getWidth(": ", font, fontSize), tempY);
									if (i != 0) {
										contentStream.showText(line);
									} else {
										i++;
									}
									contentStream.newLineAtOffset(0, -leading);
									tempY -= leading;
								}

							}
							contentStream.newLineAtOffset(
									-getWidth(softSkill.getName(), font, fontSize) - getWidth(": ", font, fontSize), 0);

						}
					}
					contentStream.endText();
				}
			}

			if (cv.isDisplayHobbies()) {
				if (interestList != null) {
					contentStream.beginText();
					if (firstSection) {
						contentStream.endText();
						contentStream.moveTo(startX, tempY);
						contentStream.lineTo(endX, tempY);
						contentStream.stroke();
						tempY -= leading;
						tempY -= 10;
						contentStream.beginText();
					} else {
						firstSection = true;
					}
					contentStream.setFont(sectionFont, sectionFontSize);
					contentStream.newLineAtOffset(center, tempY);
					contentStream.setFont(sectionFont, sectionFontSize);
					contentStream.showText(cv.getHobbies());
					contentStream.setFont(font, fontSize);
					contentStream.newLineAtOffset(-center + tempX, -sectionFontSize);
					tempY -= leading;

					for (Interest interest : interestList) {

						if (interest.getName() != null) {
							contentStream.showText(interest.getName());
							if (interest.getDescription() == null) {
								interest.setDescription(" ");
							}
							contentStream.newLineAtOffset(getWidth(interest.getName(), font, fontSize), 0);
							if (!interest.getDescription().equals(" ")) {
								contentStream.showText(": ");
							}
							String description = interest.getDescription();
							List<String> lines;
							if (interest.getName().length() > 20) {
								lines = getLinesLong(description, fontSize, font, width);
							} else {
								lines = getLines(description, fontSize, font, width);
							}
							int i = 0;
							for (String line : lines) {

								if (tempY - leading - 20 > 0) {
									contentStream.showText(line);
									contentStream.newLineAtOffset(getWidth(": ", font, fontSize), -leading);
									tempY -= leading;
									if (i++ != 0) {
										contentStream.newLineAtOffset(-getWidth(": ", font, fontSize), 0);
									}

								} else {
									if (i == 0) {
										contentStream.showText(line);
									}
									contentStream.endText();
									contentStream.close();
									page = new PDPage();
									document.addPage(page);

									contentStream = new PDPageContentStream(document, page);
									contentStream.setNonStrokingColor(color);
									contentStream.setStrokingColor(color);
									contentStream.setLineWidth(2.5f);
									contentStream.setFont(font, fontSize);
									contentStream.beginText();
									tempY = y;
									contentStream.newLineAtOffset(getWidth(interest.getName(), font, fontSize) + tempX
											+ getWidth(": ", font, fontSize), tempY);
									if (i != 0) {
										contentStream.showText(line);
									} else {
										i++;
									}
									contentStream.newLineAtOffset(0, -leading);
									tempY -= leading;
								}

							}
							contentStream.newLineAtOffset(
									-getWidth(interest.getName(), font, fontSize) - getWidth(": ", font, fontSize), 0);

						}
					}
					contentStream.endText();
				}
			}

			if (tempY == y) {
				document.removePage(page);
			}

			contentStream.close();

			String path = "user-cvs/" + user.getId() + ".pdf";

			document.save(path);

		} catch (IOException e) {
			e.printStackTrace();

		}

		finally {
			if (document != null) {
				try {
					document.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

	}

}
