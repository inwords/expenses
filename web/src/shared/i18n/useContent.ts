import content from './content';
import {AllContent, ContentLanguages} from '@/shared/types';

export const useContent = (): AllContent[ContentLanguages] => {
  return content['ru'];
};
