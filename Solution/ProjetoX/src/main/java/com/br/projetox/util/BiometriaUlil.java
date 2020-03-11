package com.br.projetox.util;

import java.io.UnsupportedEncodingException;

import org.springframework.stereotype.Component;

import com.br.projetox.exception.FingerPrintException;
import com.br.projetox.exception.FingerPrintExceptionEnum;

import CIDBio.CIDBio;
import CIDBio.ImageAndTemplate;
import CIDBio.MatchResult;
import CIDBio.RetCode;
import CIDBio.Template;

@Component
public class BiometriaUlil {
		
	/* Metodo captura a digital do paciente
	 * 
	 * @return ImageAndTemplate
	 *  
	 *  */
		public ImageAndTemplate capturarBiometria() {
			ImageAndTemplate template = null;
				CIDBio cidbio = new CIDBio();
				 ImageAndTemplate captureImgAndTemplate =  cidbio.CaptureImageAndTemplate();
				 if(captureImgAndTemplate.getRetCode() == RetCode.SUCCESS) {
					 template = captureImgAndTemplate;
				 }else {
					 throw new FingerPrintException(this.retornoDeExcessao(captureImgAndTemplate.getRetCode().name()));
				 }
			
			return template ;
		}
		
		/*Metodo captura 3 vezes a mesma digital e faz um merge 
		@return Template - retorna o template depois do merge*/
		public Template capturarTresVezesADigital() {
			Template template =null;
			ImageAndTemplate[] imgAndTemplate = new ImageAndTemplate[3] ;
				CIDBio cidbio = new CIDBio();
				for(int i = 0 ; i < imgAndTemplate.length; i++) {
					ImageAndTemplate captureTemplate = cidbio.CaptureImageAndTemplate();
					if(captureTemplate.getRetCode() == RetCode.SUCCESS && captureTemplate.getQuality() > 50) {
						imgAndTemplate[i] = captureTemplate;
					}
					else {
						throw new FingerPrintException(this.retornoDeExcessao(captureTemplate.getRetCode().name()));
					}
					
				}
				Template templateMerge = cidbio.MergeTemplates(
						imgAndTemplate[0].getTemplateString(),
						imgAndTemplate[1].getTemplateString(),
						imgAndTemplate[2].getTemplateString());
				if(templateMerge.getRetCode() == RetCode.SUCCESS) {
					template = templateMerge;
				}
				else {
					throw new FingerPrintException(this.retornoDeExcessao(templateMerge.getRetCode().name()));

				}
			
			return template;
		}
		
		/*Metodo starta o leitor biometrico
		@return RetCode - retorna SUCCESS se for startado corretamente*/
		public  RetCode iniciarOLeitor() {
			RetCode ret = CIDBio.Init();
			if(ret == RetCode.SUCCESS) {
				return ret;
			}
			else {
				throw new FingerPrintException(this.retornoDeExcessao(ret.name()));
			}
		}
		
		/*Metodo pega a descricao do enum e a retorna
		@param String fingerPrintExceptionEnum - nome do erro retornado do RetCode da bibliote CIDBio
		@return String -  retorna a mensagem especifica do erro*/
		private String retornoDeExcessao(String fingerPrintExceptionEnum){
			FingerPrintExceptionEnum fpExceptionEnum = FingerPrintExceptionEnum.valueOf(fingerPrintExceptionEnum);
			return fpExceptionEnum.getDescricao();
			}
		
		
		/*Metodo verifica se uma digital lida corresponde a passada por variavel
		@Param byte[] biometria - biometria vinda de um contrato
		@return Boolean - retorna true se sucesso e false se falhar*/
		public Boolean verificarDigital(byte[] biometria, ImageAndTemplate imgAndTemplate) throws UnsupportedEncodingException {
			Boolean returnValue = false;
				CIDBio cidbio = new CIDBio();
				if(imgAndTemplate.getRetCode() == RetCode.SUCCESS) {
					String stringTemplate = new String(biometria, "UTF-8"); 
					MatchResult score = cidbio.MatchTemplates(stringTemplate ,imgAndTemplate.getTemplateString());
					if(score.getRetCode() == RetCode.SUCCESS) {
						returnValue = true;
					}}
			return returnValue;
		}
		
		public void cancelCapture() {
			CIDBio cidbio = new CIDBio();
			cidbio.CancelCapture();
		
		}
		

}
