package com.andrecarlos.cursomc.services;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.andrecarlos.cursomc.services.exceptions.FileException;

@Service
public class ImageService {

	public BufferedImage getJpgImageFromFile(MultipartFile uploadedFile) {

		// Pegando a extensão do arquivo
		String ext = FilenameUtils.getExtension(uploadedFile.getOriginalFilename());

		if (!"png".equals(ext) && !"jpg".equals(ext)) {
			throw new FileException("Somente imagens PNG e JPG são permitidas");
		}
		
		try {
			BufferedImage img = ImageIO.read(uploadedFile.getInputStream());
			if ("png".equals(ext)) {
				img = pngToJpg(img);
			}
			return img;
		} catch (IOException e) {
			throw new FileException("Erro ao ler arquivo");
		}
	}
	
	public BufferedImage pngToJpg(BufferedImage img) {
		
		BufferedImage jpgImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		// Colocando fundo branco nos png de fundo transparente
		jpgImage.createGraphics().drawImage(img, 0, 0, Color.WHITE, null);
		return jpgImage;
	}
	
	/**
	 * Obtendo um InputStream a partir de BufferedImage, para ser utilizado no uploadFile
	 * */
	public InputStream getInputStream(BufferedImage  img, String extension) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(img, extension, os);
			return new ByteArrayInputStream(os.toByteArray());
		} catch (IOException e) {
			throw new FileException("Erro ao ler arquivo");
		}
	}
	
	/**
	 * Utilizado para recortar a imagem, deixando ela quadrada
	 * */
	public BufferedImage cropSquare(BufferedImage sourceImg) {
		
		// Pegando qual é o mínimo, altura ou largura. Para deixar a img quadrada
		int min = (sourceImg.getHeight() <= sourceImg.getWidth()) ? sourceImg.getHeight() : sourceImg.getWidth();
		return Scalr.crop(
			sourceImg, 
			(sourceImg.getWidth()/2) - (min/2), 
			(sourceImg.getHeight()/2) - (min/2), 
			min, 
			min);		
	}
	
	/**
	 * Para redimensionar uma imagem
	 * */
	public BufferedImage resize(BufferedImage sourceImg, int size) {
		// ULTRA_QUALITY para evitar ao máximo perda de qualidade da imagem
		return Scalr.resize(sourceImg, Scalr.Method.ULTRA_QUALITY, size);
	}
}
